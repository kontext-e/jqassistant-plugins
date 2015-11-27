package de.kontext_e.jqassistant.plugin.git.scanner;

import java.util.LinkedList;
import java.util.List;

public class GitCommit {
    private final String sha;
    private String author;
    private String date;
    private String message;
    private final List<CommitFile> commitFiles = new LinkedList<CommitFile>();
    private final List<GitCommit> parents = new LinkedList<GitCommit>();

    public GitCommit(final String sha) {
        this.sha = sha;
    }

    private static String buildMessage(final List<String> message) {
        StringBuilder builder = new StringBuilder();
        for (String m : message) {
            builder.append(m).append("\n");
        }
        return builder.toString();
    }

    public String getSha() {
        return sha;
    }

    public String getAuthor() {
        return author;
    }
    protected void setAuthor (final String author) {this.author = author;}

    public String getDate() {
        return date;
    }
    protected void setDate (final String date) {this.date = date;}

    public String getMessage() {
        return message;
    }
    protected void setMessage (String message) {this.message = message;}

    public List<CommitFile> getCommitFiles() {
        return commitFiles;
    }

    public List<GitCommit> getParents() {
        return parents;
    }

    @Override
    public String toString() {
        return "GitLogEntry{" +
                "sha='" + sha + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", message=" + message +
                '}';
    }

    @Override
    /* Equality is only determined by the sha */
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GitCommit that = (GitCommit) o;
        return sha.equals(that.sha);
    }

    @Override
    /* sha is the hashCode */
    public int hashCode() {
        return sha.hashCode();
    }
}
