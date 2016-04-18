package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitAuthorDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitBranchDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitChangeDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitFileDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitRepositoryDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitTagDescriptor;

/**
 * @author jn4, Kontext E GmbH
 */
public class GitScannerPlugin extends AbstractScannerPlugin<FileResource, GitRepositoryDescriptor> {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss Z");
    private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private static final Logger LOGGER = LoggerFactory.getLogger(GitScannerPlugin.class);
    public static final String GIT_RANGE = "jqassistant.plugin.git.range";

    private String range = null;

    @Override
    /**
     * Check whether this is the start of a git repository?
     *
     * If the path is "/HEAD" and the file (behind item) lives in a directory called ".git" this must be a git
     * repository and the scanner may perform it's work on it (call to "scan" method).
     */
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        File gitDirectory = item.getFile();
        LOGGER.debug ("Checking path {} / dir {}", path, gitDirectory);
        boolean isGitDir = path.endsWith("/HEAD")
                && ".git".equals(gitDirectory.toPath().toAbsolutePath().getParent().toFile().getName());
        if (!isGitDir) {
            return false;
        }
        String pathToGitProject = item.getFile().toPath().getParent().toFile().getAbsolutePath();
        LOGGER.info("Accepted Git project in '{}'", pathToGitProject);
        return true;
    }

    protected static void initGitDescriptor (final GitRepositoryDescriptor gitRepositoryDescriptor, final File file) throws IOException {
        final Path headPath = file.toPath().toAbsolutePath().normalize();
        LOGGER.debug ("Full path to Git directory HEAD is '{}'", headPath);
        final Path gitPath = headPath.getParent(); // Path of dir of /HEAD
        final String pathToGitProject = gitPath.toFile().getAbsolutePath();
        LOGGER.debug ("Full path to Git directory is '{}'", pathToGitProject);
        final Path projectPath = gitPath.getParent(); // Path of parent of dir of /HEAD
        final String projectName = projectPath.toFile().getName();
        LOGGER.debug ("Git Project name is '{}'", projectName);
        gitRepositoryDescriptor.setName(projectName);
        // For some reason the file name is presented in the neo4j console ...
        // TODO: The file name is not representative - use the project name instead?
        gitRepositoryDescriptor.setFileName(pathToGitProject);
    }

    @Override
    public GitRepositoryDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        // This is called with path = "/HEAD" since this is the only "accepted" file
        LOGGER.debug ("Scanning Git directory '{}' (call with path: '{}')", item.getFile(), path);
        Store store = scanner.getContext().getStore();
        final GitRepositoryDescriptor gitRepositoryDescriptor = store.create(GitRepositoryDescriptor.class);
        initGitDescriptor(gitRepositoryDescriptor, item.getFile());

        JGitScanner jGitScanner = new JGitScanner(gitRepositoryDescriptor.getFileName(), range);

        List<GitCommit> commits = jGitScanner.findCommits();
        List<GitBranch> branches = jGitScanner.findBranches();
        List<GitTag> tags = jGitScanner.findTags();

        addCommits(store, gitRepositoryDescriptor, commits, branches, tags);

        return gitRepositoryDescriptor;
    }

    private void addCommits(final Store store, final GitRepositoryDescriptor gitRepositoryDescriptor,
                            final List<GitCommit> gitCommits, final  List<GitBranch> branches, final List<GitTag> tags) {
        Map<String, GitAuthorDescriptor> authors = new HashMap<>();
        Map<String, GitFileDescriptor> files = new HashMap<>();
        Map<String, GitCommitDescriptor> commits = new HashMap<>();

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

            addCommitForAuthor(authors, gitCommit.getAuthor(), store, gitCommitDescriptor);

            addCommitFiles(store, gitCommit, gitCommitDescriptor, files);
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

        for (GitAuthorDescriptor gitAuthor : authors.values()) {
            gitRepositoryDescriptor.getAuthors().add(gitAuthor);
        }

        for (GitFileDescriptor gitFile : files.values()) {
            gitRepositoryDescriptor.getFiles().add(gitFile);
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
            gitRepositoryDescriptor.getBranches().add(gitBranchDescriptor);
        }

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

    private void addCommitForAuthor(final Map<String, GitAuthorDescriptor> authors, final String author, final Store store, final GitCommitDescriptor gitCommit) {
        if (null != author) {
            if(! authors.containsKey(author)) {
                LOGGER.debug ("Adding new author '{}'", author);
                GitAuthorDescriptor gitAutor = store.find(GitAuthorDescriptor.class, author);
                if (null == gitAutor) {
                    LOGGER.debug ("Author '{}' does not exist, have to create a new entity", author);
                    gitAutor = store.create(GitAuthorDescriptor.class);
                    gitAutor.setIdentString(author);
                }
                gitAutor.setName(author.substring(0, author.indexOf("<")).trim());
                gitAutor.setEmail(author.substring(author.indexOf("<")+1, author.indexOf(">")).trim());
                authors.put(author, gitAutor);
            }
            authors.get(author).getCommits().add(gitCommit);
        }
    }

    private void addCommitFiles(final Store store, final GitCommit gitCommit, final GitCommitDescriptor gitCommitDescriptor, final Map<String, GitFileDescriptor> files) {
        for (GitChange gitChange : gitCommit.getGitChanges()) {
            GitChangeDescriptor gitCommitFile = store.create(GitChangeDescriptor.class);
            gitCommitFile.setModificationKind(gitChange.getModificationKind());
            gitCommitDescriptor.getFiles().add(gitCommitFile);
            addAsGitFile(files, gitChange.getRelativePath(), gitCommitFile, store, gitCommit.getDate());
        }
    }

    private void addAsGitFile(final Map<String, GitFileDescriptor> files, String relativePath, final GitChangeDescriptor change, final Store store, final Date date) {
        GitFileDescriptor gitFileDescriptor = files.get(relativePath);
        if(gitFileDescriptor == null) {
            gitFileDescriptor = store.create(GitFileDescriptor.class);
            gitFileDescriptor.setRelativePath(relativePath);
            files.put(relativePath, gitFileDescriptor);
        }
        change.setModifies(gitFileDescriptor);
        if("A".equals(change.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setCreatedAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setCreatedAtEpoch(date.getTime());
        } else if("M".equals(change.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setLastModificationAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setLastModificationAtEpoch(date.getTime());
        } else if("D".equals(change.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setDeletedAt(DATE_TIME_FORMAT.format(date));
            gitFileDescriptor.setDeletedAtEpoch(date.getTime());
        }
    }

    private void setRange (String range) {
        this.range = range;
        LOGGER.info ("Git plugin has configured range '{}'", range);
    }

    @Override
    protected void configure() {
        super.configure();

        Map<String, Object> properties = getProperties();

        String rangeProperty = (String) properties.get(GIT_RANGE);
        if(rangeProperty != null) {
            setRange(rangeProperty);
        } else {
            rangeProperty = System.getProperty(GIT_RANGE);
            if (rangeProperty != null) {
                setRange(rangeProperty);
            }
        }
    }
}
