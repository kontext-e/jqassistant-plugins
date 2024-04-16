package de.kontext_e.jqassistant.plugin.git.scanner;

import org.eclipse.jgit.diff.DiffEntry;

import java.util.Objects;

public class GitChange {
    private final String modificationKind;
    private final String relativePath;
    private final String changeType;
    private final String oldPath;
    private final String newPath;

    public GitChange(final String changeType, final String oldPath, final String newPath) {
        this.modificationKind = changeType.substring(0, 1);
        this.relativePath = DiffEntry.ChangeType.DELETE.name().equalsIgnoreCase(changeType) ? oldPath : newPath;
        this.changeType = changeType;
        this.oldPath = oldPath;
        this.newPath = newPath;
    }

    public String getModificationKind() {
        return modificationKind;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getChangeType() {
        return changeType;
    }

    public String getOldPath() {
        return oldPath;
    }

    public String getNewPath() {
        return newPath;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GitChange gitChange = (GitChange) o;

        if (!Objects.equals(modificationKind, gitChange.modificationKind)) return false;
        if (!Objects.equals(relativePath, gitChange.relativePath)) return false;
        if (!Objects.equals(changeType, gitChange.changeType)) return false;
        if (!Objects.equals(oldPath, gitChange.oldPath)) return false;
        if (!Objects.equals(newPath, gitChange.newPath)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = modificationKind != null ? modificationKind.hashCode() : 0;
        result = 31 * result + (relativePath != null ? relativePath.hashCode() : 0);
        result = 31 * result + (changeType != null ? changeType.hashCode() : 0);
        result = 31 * result + (oldPath != null ? oldPath.hashCode() : 0);
        result = 31 * result + (newPath != null ? newPath.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GitChange{" +
               "modificationKind='" + modificationKind + '\'' +
               ", relativePath='" + relativePath + '\'' +
               ", changeType='" + changeType + '\'' +
               ", oldPath='" + oldPath + '\'' +
               ", newPath='" + newPath + '\'' +
               '}';
    }
}
