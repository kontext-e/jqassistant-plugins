package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitRepositoryDescriptor;

/**
 * @author jn4, Kontext E GmbH
 */
public class GitScannerPlugin extends AbstractScannerPlugin<FileResource, GitRepositoryDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitScannerPlugin.class);
    private static final String GIT_RANGE = "jqassistant.plugin.git.range";

    private String range = null;

    @Override
    /*
     * Check whether this is the start of a git repository.
     *
     * If the path is "/HEAD" and the file (behind item) lives in a directory called ".git" this must be a git
     * repository and the scanner may perform it's work on it (call to "scan" method).
     */
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        if(path.endsWith("/HEAD")) {
            final File gitDirectory = item.getFile();
            LOGGER.debug("Checking path {} / dir {}", path, gitDirectory);
            boolean isGitDir = ".git".equals(gitDirectory.toPath().toAbsolutePath().getParent().toFile().getName());
            if (!isGitDir) {
                return false;
            } else {
                String pathToGitProject = gitDirectory.toPath().getParent().toFile().getAbsolutePath();
                LOGGER.info("Accepted Git project in '{}'", pathToGitProject);
                return true;
            }
        }

        return false;
    }

    @Override
    public GitRepositoryDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        // This is called with path = "/HEAD" since this is the only "accepted" file
        LOGGER.debug ("Scanning Git directory '{}' (call with path: '{}')", item.getFile(), path);
        Store store = scanner.getContext().getStore();
        final GitRepositoryDescriptor gitRepositoryDescriptor = store.create(GitRepositoryDescriptor.class);
        initGitDescriptor(gitRepositoryDescriptor, item.getFile());

        new GitRepositoryScanner(store, gitRepositoryDescriptor, range).scanGitRepo();

        return gitRepositoryDescriptor;
    }

    static void initGitDescriptor(final GitRepositoryDescriptor gitRepositoryDescriptor, final File file) throws IOException {
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
