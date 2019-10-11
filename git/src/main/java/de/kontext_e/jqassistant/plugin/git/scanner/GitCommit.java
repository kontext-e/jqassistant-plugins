package de.kontext_e.jqassistant.plugin.git.scanner;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class GitCommit {
    private final String sha;
    private String author;
    private String committer;
    private Date date;
    private String message;
    private String shortMessage;
    private final List<GitChange> gitChanges = new LinkedList<>();
    private final List<GitCommit> parents = new LinkedList<>();
    private String encoding;

    GitCommit(final String sha) {
        this.sha = sha;
    }

    String getSha() {
        return sha;
    }

    public String getAuthor() {
        return author;
    }
    protected void setAuthor (final String author) {this.author = author;}

    String getCommitter() {
        return committer;
    }

    void setCommitter(final String committer) {
        this.committer = committer;
    }

    Date getDate() {
        return date;
    }
    void setDate(final Date date) {this.date = date;}

    String getMessage() {
        return message;
    }
    void setMessage(String message) {this.message = message;}

    String getShortMessage() {
        return shortMessage;
    }

    void setShortMessage(final String shortMessage) {
        this.shortMessage = shortMessage;
    }

    List<GitChange> getGitChanges() {
        return gitChanges;
    }

    List<GitCommit> getParents() {
        return parents;
    }

    void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    String getEncoding() {
        return encoding;
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

    @Override
    public String toString() {
        return "GitCommit{" +
               "sha='" + sha + '\'' +
               ", author='" + author + '\'' +
               ", committer='" + committer + '\'' +
               ", date=" + date +
               ", message='" + message + '\'' +
               ", shortMessage='" + shortMessage + '\'' +
               ", gitChanges=" + gitChanges +
               ", parents=" + parents +
               ", encoding='" + encoding + '\'' +
               '}';
    }

}
