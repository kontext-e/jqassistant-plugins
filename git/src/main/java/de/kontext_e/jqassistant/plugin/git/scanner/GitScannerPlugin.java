package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.core.store.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.VirtualFile;
import com.buschmais.jqassistant.plugin.common.impl.scanner.AbstractScannerPlugin;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitAuthorDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitCommitFileDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitDescriptor;
import de.kontext_e.jqassistant.plugin.git.store.descriptor.GitFileDescriptor;

/**
 * @author jn4, Kontext E GmbH
 */
public class GitScannerPlugin extends AbstractScannerPlugin<VirtualFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitScannerPlugin.class);
    public static final String GIT_PATH = "git.path";
    public static final String GIT_REPO = "git.repo";
    public static final String GIT_RANGE = "git.range";

    private String gitConfigurationFileName = "jqa_plugin_git.properties";

    @Override
    public boolean accepts(final VirtualFile item, final String path, final Scope scope) throws IOException {
        return path.endsWith(gitConfigurationFileName);
    }

    @Override
    public FileDescriptor scan(final VirtualFile item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        String pathToGitCommand = "git";
        String pathToGitProject = ".";
        String range = null;

        Properties configuration = new Properties();
        configuration.load(item.createStream());

        if(configuration.containsKey(GIT_PATH)) {
            pathToGitCommand = configuration.getProperty(GIT_PATH);
        }

        if(configuration.containsKey(GIT_REPO)) {
            pathToGitProject = configuration.getProperty(GIT_REPO);
        }

        if(configuration.containsKey(GIT_RANGE)) {
            range = configuration.getProperty(GIT_RANGE);
        }

        Store store = scanner.getContext().getStore();
        final GitDescriptor gitDescriptor = store.create(GitDescriptor.class);
        gitDescriptor.setName(path);
        gitDescriptor.setFileName(path);

        try {
            List<String> log = RunGitLogCommand.runGitLog(pathToGitCommand, pathToGitProject, range);
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
    protected void initialize() {
        super.initialize();
        final String property = (String) getProperties().get("jqassistant.plugin.git.filename");
        if(property != null) {
            gitConfigurationFileName = property;
        }
    }
}
