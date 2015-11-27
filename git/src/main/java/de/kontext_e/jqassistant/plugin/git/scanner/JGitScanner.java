package de.kontext_e.jqassistant.plugin.git.scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
public class JGitScanner {

    Logger logger = LoggerFactory.getLogger(JGitScanner.class);

    public List<GitCommit> scan(final String path) throws IOException {
        logger.debug("Searching for git directory from '{}' (if relative: '{}'", path, System.getProperty("user.dir"));
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = builder.setGitDir(new File(path))
                .readEnvironment() // scan environment GIT_* variables
                .build();
        logger.debug("Using Git repository in '{}'", repository.getDirectory());

        List<GitCommit> result = new LinkedList<GitCommit>();

        ObjectId head = repository.resolve("HEAD");
        logger.debug("Found head: {}", head);

        RevWalk rw = new RevWalk(repository);

        try (Git git = new Git(repository)) {
            Iterable<RevCommit> commits = null;
            commits = git.log().all().call();

            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
            df.setRepository(repository);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);

            // TODO Improve this: It does not make sense to forward dates as Strings ...
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

            for (RevCommit commit : commits) {
                logger.debug("Commit-Message: '{}'", commit.getShortMessage());

                RevCommit parent = null;
                List<CommitFile> commitedFiles = new LinkedList<CommitFile>();

                if (commit.getParentCount() > 0) {
                    parent = rw.parseCommit(commit.getParent(0).getId());

                    List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
                    for (DiffEntry diff : diffs) {
                        logger.debug("changeType={}, path={}",
                                diff.getChangeType().name(), diff.getNewPath());
                        CommitFile commitFile = new CommitFile(diff.getChangeType().toString(), diff.getNewPath());
                        commitedFiles.add(commitFile);
                    }
                }

                String author = commit.getAuthorIdent().getName() + " <" +
                        commit.getAuthorIdent().getEmailAddress() + ">";
                String date = dateFormat.format (new Date(commit.getCommitTime()));
                GitCommit gitCommit = new GitCommit(
                        commit.getId().toString() // SHA
                        ,  author // Author
                        // TODO Add Commiter also!
                        , date // Date
                        , commitedFiles
                        , commit.getFullMessage()
                );
                result.add(gitCommit);
            }
        } catch (GitAPIException e) {
            throw new IllegalStateException("Could not read logs from Git repository '" + path + "'", e);
        } finally {
            rw.close();
        }

        logger.debug("Found #{} commits", result.size());
        return result;
    }
}
