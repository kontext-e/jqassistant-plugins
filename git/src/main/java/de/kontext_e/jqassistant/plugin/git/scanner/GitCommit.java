package de.kontext_e.jqassistant.plugin.git.scanner;

import java.util.LinkedList;
import java.util.List;

public class GitCommit {
    private final String sha;
    private final String author;
    private final String date;
    private final List<String> message = new LinkedList<String>();
    private final List<CommitFile> commitFiles = new LinkedList<CommitFile>();

    public GitCommit(final String sha, final String author, final String date, final List<CommitFile> currentCommitFiles, final List<String> currentMessage) {
        this.sha = sha;
        this.author = author;
        this.date = date;
        this.commitFiles.addAll(currentCommitFiles);
        this.message.addAll(currentMessage);
    }

    public String getSha() {
        return sha;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public List<String> getMessage() {
        return message;
    }

    public List<CommitFile> getCommitFiles() {
        return commitFiles;
    }

    @Override
    public String toString() {
        return "GitLogEntry{" +
                "sha='" + sha + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", message=" + message +
                ", commitFiles=" + commitFiles +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final GitCommit that = (GitCommit) o;

        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        if (commitFiles != null ? !commitFiles.equals(that.commitFiles) : that.commitFiles != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (sha != null ? !sha.equals(that.sha) : that.sha != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sha != null ? sha.hashCode() : 0;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (commitFiles != null ? commitFiles.hashCode() : 0);
        return result;
    }
}
