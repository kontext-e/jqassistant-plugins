package de.kontext_e.jqassistant.plugin.git.scanner;

/**
 * A Git Branch.
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 * @since 1.1.0
 */
public class GitBranch {
    private final String name;
    private final String commitSha;

    public GitBranch (String name, String commitSha) {
        this.name = name;
        this.commitSha = commitSha;
    }

    public String getName() {
        return name;
    }

    public String getCommitSha() {
        return commitSha;
    }
}
