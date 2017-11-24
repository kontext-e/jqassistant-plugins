package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitAuthorDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitBranchDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitChangeDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitFileDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitRepositoryDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitTagDescriptor;

class GitRepositoryScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepositoryScanner.class);

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss Z");
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private final Store store;
    private final GitRepositoryDescriptor gitRepositoryDescriptor;
    private String range;
    private final Map<String, GitAuthorDescriptor> authors = new HashMap<>();
    private final Map<String, GitFileDescriptor> files = new HashMap<>();
    private final Map<String, GitCommitDescriptor> commits = new HashMap<>();
    private final List<GitCommit> gitCommits = new ArrayList<>();

    GitRepositoryScanner(final Store store, final GitRepositoryDescriptor gitRepositoryDescriptor, final String range) {
        this.store = store;
        this.gitRepositoryDescriptor = gitRepositoryDescriptor;
        this.range = range;
    }

    void scanGitRepo() throws IOException {
        JGitScanner jGitScanner = new JGitScanner(gitRepositoryDescriptor.getFileName(), range);

        gitCommits.addAll(jGitScanner.findCommits());

        addCommits();
        addBranches(jGitScanner.findBranches());
        addTags(jGitScanner.findTags());

        authors.values().forEach(gitAuthor -> gitRepositoryDescriptor.getAuthors().add(gitAuthor));
        files.values().forEach(gitFile -> gitRepositoryDescriptor.getFiles().add(gitFile));

        GitBranch head = jGitScanner.findHead();
        GitCommitDescriptor headDescriptor = commits.get(head.getCommitSha());
        gitRepositoryDescriptor.setHead(headDescriptor);
    }

    private void addCommits() {
        // First pass: Add the commits to the graph
        for (GitCommit gitCommit : gitCommits) {
            GitCommitDescriptor gitCommitDescriptor = store.create(GitCommitDescriptor.class);
            String sha = gitCommit.getSha();
            LOGGER.debug ("Adding new Commit '{}'", sha);
            commits.put(sha, gitCommitDescriptor);

            gitCommitDescriptor.setSha(gitCommit.getSha());
            gitCommitDescriptor.setAuthor(gitCommit.getAuthor());
            gitCommitDescriptor.setDate(DATE_FORMAT.format(gitCommit.getDate()));
            gitCommitDescriptor.setMessage(gitCommit.getMessage());
            gitCommitDescriptor.setEpoch(gitCommit.getDate().getTime());
            gitCommitDescriptor.setTime(TIME_FORMAT.format(gitCommit.getDate()));

            gitRepositoryDescriptor.getCommits().add(gitCommitDescriptor);

            addCommitForAuthor(authors, gitCommit.getAuthor(), gitCommitDescriptor);

            addCommitFiles(gitCommit, gitCommitDescriptor, files);
        }

        // Second pass: Add the parents to the graph
        for (GitCommit gitCommit : gitCommits) {
            String sha = gitCommit.getSha();
            GitCommitDescriptor gitCommitDescriptor = commits.get(sha);
            for (GitCommit parent : gitCommit.getParents()) {
                String parentSha = parent.getSha();
                GitCommitDescriptor parentCommit = commits.get(parentSha);
                if (null == parentCommit) {
                    LOGGER.warn ("Cannot add (parent) commit with SHA '{}' (excluded by range?)", parentSha);
                } else {
                    gitCommitDescriptor.getParents().add(parentCommit);
                }
            }
        }
    }

    private void addBranches(List<GitBranch> branches) throws IOException {
        for (GitBranch gitBranch : branches) {
            GitBranchDescriptor gitBranchDescriptor = store.create(GitBranchDescriptor.class);
            String name = gitBranch.getName();
            name = name.replaceFirst("refs/", "");
            String sha = gitBranch.getCommitSha();
            LOGGER.debug ("Adding new Branch '{}' with Head '{}'", name, sha);
            gitBranchDescriptor.setName(name);
            GitCommitDescriptor gitCommitDescriptor = commits.get(sha);
            if (null == gitCommitDescriptor) {
                LOGGER.warn ("Cannot retrieve commit '{}' for branch '{}'", sha, name);
            }
            gitBranchDescriptor.setHead(gitCommitDescriptor);
            gitRepositoryDescriptor.getBranches().add(gitBranchDescriptor);
        }
    }

    private void addTags(List<GitTag> tags) throws IOException {
        for (GitTag gitTag : tags) {
            GitTagDescriptor gitTagDescriptor = store.create(GitTagDescriptor.class);
            String label = gitTag.getLabel();
            label = label.replaceFirst("refs/tags/", "");
            String sha = gitTag.getCommitSha();
            LOGGER.debug ("Adding new Tag '{}' with Commit '{}'", label, sha);
            gitTagDescriptor.setLabel(label);
            GitCommitDescriptor gitCommitDescriptor = commits.get(sha);
            if (null == gitCommitDescriptor) {
                LOGGER.warn ("Cannot retrieve commit '{}' for tag '{}'", sha, label);
            }
            gitTagDescriptor.setCommit(gitCommitDescriptor);
            gitRepositoryDescriptor.getTags().add(gitTagDescriptor);
        }
    }

    private void addCommitForAuthor(final Map<String, GitAuthorDescriptor> authors, final String author, final GitCommitDescriptor gitCommit) {
        if (null != author) {
            if(! authors.containsKey(author)) {
                LOGGER.debug ("Adding new author '{}'", author);
                GitAuthorDescriptor gitAuthor = store.find(GitAuthorDescriptor.class, author);
                if (null == gitAuthor) {
                    LOGGER.debug ("Author '{}' does not exist, have to create a new entity", author);
                    gitAuthor = store.create(GitAuthorDescriptor.class);
                    gitAuthor.setIdentString(author);
                }
                gitAuthor.setName(author.substring(0, author.indexOf("<")).trim());
                gitAuthor.setEmail(author.substring(author.indexOf("<")+1, author.indexOf(">")).trim());
                authors.put(author, gitAuthor);

            }
            authors.get(author).getCommits().add(gitCommit);
        }
    }

    private void addCommitFiles(final GitCommit gitCommit, final GitCommitDescriptor gitCommitDescriptor, final Map<String, GitFileDescriptor> files) {
        for (GitChange gitChange : gitCommit.getGitChanges()) {
            GitChangeDescriptor gitCommitFile = store.create(GitChangeDescriptor.class);
            gitCommitFile.setModificationKind(gitChange.getModificationKind());
            gitCommitDescriptor.getFiles().add(gitCommitFile);
            addAsGitFile(files, gitChange, gitCommitFile, gitCommit.getDate());
        }
    }

    private void addAsGitFile(final Map<String, GitFileDescriptor> files, GitChange gitChange, final GitChangeDescriptor gitChangeDescriptor, final Date date) {
        final GitFileDescriptor gitFileDescriptor = getOrCreateGitFileDescriptor(files, gitChange.getRelativePath());

        gitChangeDescriptor.setModifies(gitFileDescriptor);

        /* Modification Kinds
         * Added (A),
         * Copied (C),
         * Deleted (D),
         * Modified (M),
         * Renamed (R)
         */
        if(isAddChange(gitChangeDescriptor)) {
            gitFileDescriptor.setCreatedAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setCreatedAtEpoch(date.getTime());
            gitChangeDescriptor.setCreates(gitFileDescriptor);
        } else if(isUpdateChange(gitChangeDescriptor)) {
            gitFileDescriptor.setLastModificationAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setLastModificationAtEpoch(date.getTime());
            gitChangeDescriptor.setUpdates(gitFileDescriptor);
        } else if(isDeleteChange(gitChangeDescriptor)) {
            gitFileDescriptor.setDeletedAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setDeletedAtEpoch(date.getTime());
            gitChangeDescriptor.setDeletes(gitFileDescriptor);
        } else if(isRenameChange(gitChangeDescriptor)) {
            final GitFileDescriptor oldFile = getOrCreateGitFileDescriptor(files, gitChange.getOldPath());
            final GitFileDescriptor newFile = getOrCreateGitFileDescriptor(files, gitChange.getNewPath());
            oldFile.setHasNewName(newFile);
            gitChangeDescriptor.setRenames(oldFile);
            gitChangeDescriptor.setDeletes(oldFile);
            gitChangeDescriptor.setCreates(newFile);
        } else if(isCopyChange(gitChangeDescriptor)) {
            final GitFileDescriptor oldFile = getOrCreateGitFileDescriptor(files, gitChange.getOldPath());
            final GitFileDescriptor newFile = getOrCreateGitFileDescriptor(files, gitChange.getNewPath());
            newFile.setCopyOf(oldFile);
            gitChangeDescriptor.setCopies(oldFile);
            gitChangeDescriptor.setCreates(newFile);
        }
    }

    private boolean isCopyChange(final GitChangeDescriptor gitChangeDescriptor) {
        return "C".equals(gitChangeDescriptor.getModificationKind().toUpperCase());
    }

    private boolean isRenameChange(final GitChangeDescriptor gitChangeDescriptor) {
        return "R".equals(gitChangeDescriptor.getModificationKind().toUpperCase());
    }

    private boolean isDeleteChange(final GitChangeDescriptor gitChangeDescriptor) {
        return "D".equals(gitChangeDescriptor.getModificationKind().toUpperCase());
    }

    private boolean isUpdateChange(final GitChangeDescriptor gitChangeDescriptor) {
        return "M".equals(gitChangeDescriptor.getModificationKind().toUpperCase());
    }

    private boolean isAddChange(final GitChangeDescriptor gitChangeDescriptor) {
        return "A".equals(gitChangeDescriptor.getModificationKind().toUpperCase());
    }

    private GitFileDescriptor getOrCreateGitFileDescriptor(final Map<String, GitFileDescriptor> files, String relativePath) {
        GitFileDescriptor gitFileDescriptor = files.get(relativePath);
        if(gitFileDescriptor == null) {
            gitFileDescriptor = store.create(GitFileDescriptor.class);
            gitFileDescriptor.setRelativePath(relativePath);
            files.put(relativePath, gitFileDescriptor);
        }
        return gitFileDescriptor;
    }

}
