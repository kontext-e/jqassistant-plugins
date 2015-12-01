package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitTagDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitAuthorDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitBranchDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitFileDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitFileDescriptor;

/**
 * @author jn4, Kontext E GmbH
 */
public class GitScannerPlugin extends AbstractScannerPlugin<FileResource, GitDescriptor> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss Z");
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private static final Logger LOGGER = LoggerFactory.getLogger(GitScannerPlugin.class);
    public static final String GIT_RANGE = "jqassistant.plugin.git.range";

    private String pathToGitProject = ".";
    private String range = null;

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        boolean isGitDir = path.endsWith("index") && ".git".equals(item.getFile().getParent());
        if(isGitDir) {
            pathToGitProject = item.getFile().toPath().getParent().toFile().getAbsolutePath();
            LOGGER.info("Path to git project is "+pathToGitProject);
        }
        return isGitDir;
    }

    @Override
    public GitDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        LOGGER.debug("Git plugin scans '{}' in '{}'", path, pathToGitProject);
        Store store = scanner.getContext().getStore();
        final GitDescriptor gitDescriptor = store.create(GitDescriptor.class);
        gitDescriptor.setName(path);
        gitDescriptor.setFileName(path);

        JGitScanner jGitScanner = new JGitScanner(pathToGitProject);

        List<GitCommit> commits = jGitScanner.findCommits();
        List<GitBranch> branches = jGitScanner.findBranches();
        List<GitTag> tags = jGitScanner.findTags();

        addCommits(store, gitDescriptor, commits, branches, tags);

        return gitDescriptor;
    }

    private void addCommits(final Store store, final GitDescriptor gitDescriptor,
                            final List<GitCommit> gitCommits, final  List<GitBranch> branches, final List<GitTag> tags) {
        Map<String, GitAuthorDescriptor> authors = new HashMap<String,GitAuthorDescriptor>();
        Map<String, GitFileDescriptor> files = new HashMap<String,GitFileDescriptor>();
        Map<String, GitCommitDescriptor> commits = new HashMap<String,GitCommitDescriptor>();

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

            gitDescriptor.getCommits().add(gitCommitDescriptor);

            addCommitForAuthor(authors, gitCommit.getAuthor(), store, gitCommitDescriptor);

            addCommitFiles(store, gitCommit, gitCommitDescriptor, files);
        }

        // Second pass: Add the parents to the graph
        for (GitCommit gitCommit : gitCommits) {
            String sha = gitCommit.getSha();
            GitCommitDescriptor gitCommitDescriptor = commits.get(sha);
            for (GitCommit parent : gitCommit.getParents()) {
                String parentSha = parent.getSha();
                gitCommitDescriptor.getParents().add(commits.get(parentSha));
            }
        }

        for (GitAuthorDescriptor gitAuthor : authors.values()) {
            gitDescriptor.getAuthors().add(gitAuthor);
        }

        for (GitFileDescriptor gitFile : files.values()) {
            gitDescriptor.getFiles().add(gitFile);
        }

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
            gitDescriptor.getBranches().add(gitBranchDescriptor);
        }

        for (GitTag gitTag : tags) {
            GitTagDescriptor gitTagDescriptor = store.create(GitTagDescriptor.class);
            String label = gitTag.getLabel();
            label = label.replaceFirst("refs/tags/", "");
            String sha = gitTag.getCommitSha();
            LOGGER.debug ("Adding new Branch '{}' with Head '{}'", label, sha);
            gitTagDescriptor.setLabel(label);
            GitCommitDescriptor gitCommitDescriptor = commits.get(sha);
            if (null == gitCommitDescriptor) {
                LOGGER.warn ("Cannot retrieve commit '{}' for branch '{}'", sha, label);
            }
            gitTagDescriptor.setCommit(gitCommitDescriptor);
            gitDescriptor.getTags().add(gitTagDescriptor);
        }
    }

    private void addCommitForAuthor(final Map<String, GitAuthorDescriptor> authors, final String author, final Store store, final GitCommitDescriptor gitCommit) {
        if (null != author) {
            if(! authors.containsKey(author)) {
                LOGGER.debug ("Adding new author '{}'", author);
                GitAuthorDescriptor gitAutor = store.create(GitAuthorDescriptor.class);
                gitAutor.setIdentString(author);
                gitAutor.setName(author.substring(0, author.indexOf("<")).trim());
                gitAutor.setEmail(author.substring(author.indexOf("<")+1, author.indexOf(">")).trim());
                authors.put(author, gitAutor);
            }
            authors.get(author).getCommits().add(gitCommit);
        }
    }

    private void addCommitFiles(final Store store, final GitCommit gitCommit, final GitCommitDescriptor gitCommitDescriptor, final Map<String, GitFileDescriptor> files) {
        for (CommitFile commitFile : gitCommit.getCommitFiles()) {
            GitCommitFileDescriptor gitCommitFile = store.create(GitCommitFileDescriptor.class);

            gitCommitFile.setModificationKind(commitFile.getModificationKind());
            gitCommitFile.setRelativePath(commitFile.getRelativePath());

            gitCommitDescriptor.getFiles().add(gitCommitFile);

            addAsGitFile(files, gitCommitFile, store, gitCommit.getDate());
        }
    }

    private void addAsGitFile(final Map<String, GitFileDescriptor> files, final GitCommitFileDescriptor commitFile, final Store store, final Date date) {
        if(!files.containsKey(commitFile.getRelativePath())) {
            GitFileDescriptor gitFile = store.create(GitFileDescriptor.class);
            gitFile.setRelativePath(commitFile.getRelativePath());
            files.put(commitFile.getRelativePath(), gitFile);
        }

        GitFileDescriptor gitFileDescriptor = files.get(commitFile.getRelativePath());
        gitFileDescriptor.getCommitFiles().add(commitFile);

        if("A".equals(commitFile.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setCreatedAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setCreatedAtEpoch(date.getTime());
        } else if("M".equals(commitFile.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setLastModificationAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setLastModificationAtEpoch(date.getTime());
        } else if("D".equals(commitFile.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setDeletedAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setDeletedAtEpoch(date.getTime());
        }
    }

    @Override
    protected void configure() {
        super.configure();

        Map<String, Object> properties = getProperties();

        final String rangeProperty = (String) properties.get(GIT_RANGE);
        if(rangeProperty != null) {
            range = rangeProperty;
        }
        if(System.getProperty(GIT_RANGE) != null) {
            throw new RuntimeException ("Ranges are currently not supported!");
//            range = System.getProperty(GIT_RANGE);
        }
        LOGGER.debug("Git plugin has configured range "+range);
    }
}
