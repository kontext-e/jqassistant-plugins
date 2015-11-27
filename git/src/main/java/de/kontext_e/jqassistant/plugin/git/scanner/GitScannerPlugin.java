package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.IOException;
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

        List<GitCommit> commits = jGitScanner.scan();
        addCommits(store, gitDescriptor, commits);

        return gitDescriptor;
    }

    private void addCommits(final Store store, final GitDescriptor gitDescriptor, final List<GitCommit> gitCommits) {
        Map<String, GitAuthorDescriptor> authors = new HashMap<String,GitAuthorDescriptor>();
        Map<String, GitFileDescriptor> files = new HashMap<String,GitFileDescriptor>();
        Map<String, GitCommitDescriptor> commits = new HashMap<String,GitCommitDescriptor>();

        // First pass: Add the commits to the graph
        for (GitCommit gitCommit : gitCommits) {
            GitCommitDescriptor gitCommitDescriptor = store.create(GitCommitDescriptor.class);
            String sha = gitCommit.getSha();
            commits.put(sha, gitCommitDescriptor);

            gitCommitDescriptor.setSha(gitCommit.getSha());
            gitCommitDescriptor.setAuthor(gitCommit.getAuthor());
            gitCommitDescriptor.setDate(gitCommit.getDate());
            gitCommitDescriptor.setMessage(gitCommit.getMessage());
            gitCommitDescriptor.setEpoch(epochFromDate(gitCommit.getDate()));
            gitCommitDescriptor.setTime(gitCommit.getDate().substring(11, 19));

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

    @Override
    protected void configure() {
        super.configure();

        Map<String, Object> properties = getProperties();

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
