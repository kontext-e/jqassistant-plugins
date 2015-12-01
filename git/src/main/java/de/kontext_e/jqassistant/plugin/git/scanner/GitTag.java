package de.kontext_e.jqassistant.plugin.git.scanner;

/**
 * A Git Tag.
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 * @since 1.1.0
 */
public class GitTag {
    private String label;
    private String commitSha;

    public GitTag(String label, String commitSha) {
        this.label = label;
        this.commitSha = commitSha;
    }

    public String getLabel() {
        return label;
    }

    public String getCommitSha() {
        return commitSha;
    }
}
