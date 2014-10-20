package de.kontext_e.jqassistant.plugin.git.scanner;

public class CommitFile {
    private final String modificationKind;
    private final String relativePath;

    public CommitFile(final String modificationKind, final String relativePath) {
        this.modificationKind = modificationKind;
        this.relativePath = relativePath;
    }

    public CommitFile(final String line) {
        this(line.substring(0, 1), line.substring(2).trim());
    }

    public String getModificationKind() {
        return modificationKind;
    }

    public String getRelativePath() {
        return relativePath;
    }

    @Override
    public String toString() {
        return "CommitFile{" +
                "modificationKind='" + modificationKind + '\'' +
                ", relativePath='" + relativePath + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final CommitFile that = (CommitFile) o;

        if (modificationKind != null ? !modificationKind.equals(that.modificationKind) : that.modificationKind != null)
            return false;
        if (relativePath != null ? !relativePath.equals(that.relativePath) : that.relativePath != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = modificationKind != null ? modificationKind.hashCode() : 0;
        result = 31 * result + (relativePath != null ? relativePath.hashCode() : 0);
        return result;
    }
}
