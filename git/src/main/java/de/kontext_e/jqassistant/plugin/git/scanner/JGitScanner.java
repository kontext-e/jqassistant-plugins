package de.kontext_e.jqassistant.plugin.git.scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * A Scanner based on Eclipse JGit.
 *
 * Check out JGit documentation
 * <ul>
 *     <li><a href="https://eclipse.org/jgit/">JGit project</a></li>
 *     <li><a href="https://github.com/centic9/jgit-cookbook">JGit cookbook</a></li>
 * </ul>
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 * @since 1.1.0
 */
// TODO: Rename this! In fact it is not a Scanner but a Repository!
public class JGitScanner {

    private static final Logger logger = LoggerFactory.getLogger(JGitScanner.class);

    private String path = null;
    private String range = null;
    private Map<String,GitCommit> commits = new HashMap<>();

    private GitCommit retrieveCommit (String sha) {
        if (!commits.containsKey(sha)) {
            commits.put(sha, new GitCommit (sha));
        }
        return commits.get(sha);
    }

    public JGitScanner (final String path, String range) {
        this.path = path;
        this.range = range;
    }

    protected static LogCommand getLogWithOrWithOutRange (Git git, String range) throws IOException {
        LogCommand result = git.log();

        if (null == range) {
            result = result.all();
        } else {
            int firstDot = range.indexOf('.');
            if (firstDot <= 0) {
                throw new IllegalArgumentException ("Git range must start like '<rev specification>..'");
            }
            int lastDot = range.lastIndexOf(".");
            if (lastDot - firstDot != 1) {
                throw new IllegalArgumentException ("Git range specials ('three dot notation' etc.) are not supported!");
            }
            String sinceString = range.substring(0, firstDot);
            String untilString = lastDot + 1 < range.length() ? range.substring(lastDot + 1) : "HEAD";
            logger.debug ("Using range from '{}' to '{}'", sinceString, untilString);
            AnyObjectId since = git.getRepository().resolve(sinceString);
            if (null == since) {
                throw new IllegalArgumentException("Could not retrieve 'since' Range part '" + sinceString + "'");
            }
            AnyObjectId until = git.getRepository().resolve(untilString);
            if (null == until) {
                throw new IllegalArgumentException("Could not retrieve 'until' Range part '" + untilString + "'");
            }
            result = result.addRange(since, until);
        }

        return result;
    }

    public List<GitCommit> findCommits() throws IOException {
        Repository repository = getRepository();

        List<GitCommit> result = new LinkedList<>();

        ObjectId head = repository.resolve("HEAD");
        logger.debug("Found head: {}", head);

        RevWalk rw = new RevWalk(repository);

        try (Git git = new Git(repository)) {
            LogCommand logCommand = getLogWithOrWithOutRange(git, range);
            Iterable<RevCommit> commits = logCommand.call();

            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
            df.setRepository(repository);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);

            for (RevCommit commit : commits) {
                logger.debug("Commit-Message: '{}'", commit.getShortMessage());
                final String author = commit.getAuthorIdent().getName() + " <" +
                        commit.getAuthorIdent().getEmailAddress() + ">";
                final Date date = new Date(1000 * (long) commit.getCommitTime());
                final GitCommit gitCommit = retrieveCommit(ObjectId.toString(commit.getId()));
                gitCommit.setAuthor(author);
                // TODO Add Committer also!
                gitCommit.setDate(date);
                gitCommit.setMessage(commit.getFullMessage());

                for (int i = 0; i < commit.getParentCount(); i++) {
                    ObjectId parentId = commit.getParent(i).getId();
                    RevCommit parent = rw.parseCommit(parentId);

                    List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
                    for (DiffEntry diff : diffs) {
                        final String changeType = diff.getChangeType().toString().substring(0, 1);
                        final String oldPath = diff.getOldPath();
                        final String newPath = diff.getNewPath();
                        final String path = "D".equalsIgnoreCase(changeType) ? oldPath : newPath;
                        logger.debug("changeType={}, path={}", changeType, path);
                        final GitChange gitChange = new GitChange(changeType, path);
                        gitCommit.getGitChanges().add(gitChange);
                    }

                    String parentSha = ObjectId.toString(parentId);
                    final GitCommit parentCommit = retrieveCommit(parentSha);
                    gitCommit.getParents().add(parentCommit);
                }

                result.add(gitCommit);
            }
        } catch (GitAPIException e) {
            throw new IllegalStateException("Could not read logs from Git repository '" + path + "'", e);
        } finally {
            rw.close();
            repository.close();
        }

        logger.debug("Found #{} commits", result.size());
        return result;
    }

    private Repository getRepository() throws IOException {
        logger.debug("Opening repository for git directory '{}'", path);
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder
                .setGitDir(new File(path))
                .readEnvironment() // scan environment GIT_* variables
                .build();
        logger.debug("Using Git repository in '{}'", repository.getDirectory());
        return repository;
    }

    public List<GitBranch> findBranches () throws IOException {
        Repository repository = getRepository();

        List<GitBranch> result = new LinkedList<>();

        try (Git git = new Git(repository)) {
            List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref branchRef : branches) {
                GitBranch newBranch = new GitBranch (branchRef.getName(), ObjectId.toString(branchRef.getObjectId()));
                result.add (newBranch);
            }
        } catch (GitAPIException e) {
            throw new IllegalStateException("Could not read branches from Git repository '" + path + "'", e);
        } finally {
            repository.close();
        }

        return result;
    }

    private RevCommit resolveFirstCommitForTag (Git git, Ref tagRef) throws IOException, GitAPIException {
        LogCommand log = git.log();
        Ref peeledRef = git.getRepository().peel(tagRef);
        if(peeledRef.getPeeledObjectId() != null) {
            log.add(peeledRef.getPeeledObjectId());
        } else {
            log.add(tagRef.getObjectId());
        }

        Iterable<RevCommit> logs = log.call();

        return logs.iterator().next();
    }

    public List<GitTag> findTags () throws IOException {
        Repository repository = getRepository();

        List<GitTag> result = new LinkedList<>();

        try (Git git = new Git(repository)) {
            List<Ref> tags = git.tagList().call();
            for (Ref tagRef : tags) {
                String label = tagRef.getName();
                RevCommit firstCommit = resolveFirstCommitForTag (git, tagRef);
                String objectId = ObjectId.toString(firstCommit);
                logger.debug ("Found Tag '{}' (name = '{}', sha = '{}')", tagRef, label, objectId);
                GitTag newTag = new GitTag (label, objectId);
                result.add (newTag);
            }
        } catch (GitAPIException e) {
            throw new IllegalStateException("Could not read tags from Git repository '" + path + "'", e);
        } finally {
            repository.close();
        }

        return result;
    }

}
