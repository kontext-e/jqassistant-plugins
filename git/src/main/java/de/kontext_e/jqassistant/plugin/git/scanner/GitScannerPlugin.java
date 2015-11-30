package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitFileDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitFileDescriptor;

/**
 * @author jn4, Kontext E GmbH
 */
public class GitScannerPlugin extends AbstractScannerPlugin<FileResource, GitDescriptor> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitScannerPlugin.class);
    public static final String GIT_PATH = "jqassistant.plugin.git.path";
    public static final String GIT_RANGE = "jqassistant.plugin.git.range";

    private String pathToGitCommand = "git";
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

    protected static void initGitDescriptor (final GitDescriptor gitDescriptor, final File file) throws IOException {
        final Path headPath = file.toPath().toAbsolutePath().normalize();
        LOGGER.debug ("Full path to Git directory HEAD is '{}'", headPath);
        final Path gitPath = headPath.getParent(); // Path of dir of /HEAD
        final String pathToGitProject = gitPath.toFile().getAbsolutePath();
        LOGGER.debug ("Full path to Git directory is '{}'", pathToGitProject);
        final Path projectPath = gitPath.getParent(); // Path of parent of dir of /HEAD
        final String projectName = projectPath.toFile().getName();
        LOGGER.debug ("Git Project name is '{}'", projectName);
        gitDescriptor.setName(projectName);
        // For some reason the file name is presented in the neo4j console ...
        // TODO: The file name is not representative - use the project name instead?
        gitDescriptor.setFileName(pathToGitProject);
    }

    @Override
    public GitDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        // This is called with path = "/HEAD" since this is the only "accepted" file
        LOGGER.debug ("Scanning Git directory '{}' (call with path: '{}')", item.getFile(), path);
        final Store store = scanner.getContext().getStore();
        final GitDescriptor gitDescriptor = store.create(GitDescriptor.class);
        initGitDescriptor(gitDescriptor, item.getFile());
        try {
            List<String> log = RunGitLogCommand.runGitLog(pathToGitCommand, gitDescriptor.getFileName(), range);
            List<GitCommit> parse = new Parser().parse(log);
            addCommits(store, gitDescriptor, parse);
        } catch (Exception e) {
            LOGGER.error("Unable to scan git repository: "+e.toString());
            LOGGER.debug("Exception details:", e);
        }

        return gitDescriptor;
    }

    private void addCommits(final Store store, final GitDescriptor gitDescriptor, final List<GitCommit> parse) {
        Map<String, GitAuthorDescriptor> authors = new HashMap<>();
        Map<String, GitFileDescriptor> files = new HashMap<>();

        for (GitCommit gitCommit : parse) {
            GitCommitDescriptor gitCommitDescriptor = store.create(GitCommitDescriptor.class);

            gitCommitDescriptor.setSha(gitCommit.getSha());
            gitCommitDescriptor.setAuthor(gitCommit.getAuthor());
            gitCommitDescriptor.setDate(gitCommit.getDate());
            gitCommitDescriptor.setMessage(buildMessage(gitCommit.getMessage()));
            gitCommitDescriptor.setEpoch(epochFromDate(gitCommit.getDate()));
            gitCommitDescriptor.setTime(gitCommit.getDate().substring(11, 19));

            gitDescriptor.getCommits().add(gitCommitDescriptor);

            addCommitForAuthor(authors, gitCommit.getAuthor(), store, gitCommitDescriptor);

            addCommitFiles(store, gitCommit, gitCommitDescriptor, files);
        }

        for (GitAuthorDescriptor gitAuthor : authors.values()) {
            gitDescriptor.getAuthors().add(gitAuthor);
        }

        for (GitFileDescriptor gitFile : files.values()) {
            gitDescriptor.getFiles().add(gitFile);
        }

    }

    private void addCommitForAuthor(final Map<String, GitAuthorDescriptor> authors, final String author, final Store store, final GitCommitDescriptor gitCommit) {
        if(! authors.containsKey(author)) {
            GitAuthorDescriptor gitAutor = store.create(GitAuthorDescriptor.class);
            gitAutor.setIdentString(author);
            gitAutor.setName(author.substring(0, author.indexOf("<")).trim());
            gitAutor.setEmail(author.substring(author.indexOf("<")+1, author.indexOf(">")).trim());
            authors.put(author, gitAutor);
        }
        authors.get(author).getCommits().add(gitCommit);
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

    private void addAsGitFile(final Map<String, GitFileDescriptor> files, final GitCommitFileDescriptor commitFile, final Store store, final String date) {
        if(!files.containsKey(commitFile.getRelativePath())) {
            GitFileDescriptor gitFile = store.create(GitFileDescriptor.class);
            gitFile.setRelativePath(commitFile.getRelativePath());
            files.put(commitFile.getRelativePath(), gitFile);
        }

        GitFileDescriptor gitFileDescriptor = files.get(commitFile.getRelativePath());
        gitFileDescriptor.getCommitFiles().add(commitFile);

        if("A".equals(commitFile.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setCreatedAt(date);
            gitFileDescriptor.setCreatedAtEpoch(epochFromDate(date));
        } else if("M".equals(commitFile.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setLastModificationAt(date);
            gitFileDescriptor.setLastModificationAtEpoch(epochFromDate(date));
        } else if("D".equals(commitFile.getModificationKind().toUpperCase())) {
            gitFileDescriptor.setDeletedAt(date);
            gitFileDescriptor.setDeletedAtEpoch(epochFromDate(date));
        }
    }

    private Long epochFromDate(final String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z").parse(date).getTime();
        } catch (ParseException e) {
            LOGGER.warn("Could not parse date '"+date+"'", e);
            return null;
        }
    }

    private String buildMessage(final List<String> message) {
        StringBuilder builder = new StringBuilder();
        for (String m : message) {
            builder.append(m).append("\n");
        }
        return builder.toString();
    }

    @Override
    protected void configure() {
        super.configure();

        Map<String, Object> properties = getProperties();
        final String pathProperty = (String) properties.get(GIT_PATH);
        if(pathProperty != null) {
            pathToGitCommand = pathProperty;
        }
        if(System.getProperty(GIT_PATH) != null) {
            pathToGitCommand = System.getProperty(GIT_PATH);
        }
        LOGGER.debug("Git plugin uses this git installation: "+pathToGitCommand);

        final String rangeProperty = (String) properties.get(GIT_RANGE);
        if(rangeProperty != null) {
            range = rangeProperty;
        }
        if(System.getProperty(GIT_RANGE) != null) {
            range = System.getProperty(GIT_RANGE);
        }
        LOGGER.debug("Git plugin has configured range "+range);
    }
}
