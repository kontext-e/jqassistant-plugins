package de.kontext_e.jqassistant.plugin.git.scanner;

import java.util.LinkedList;
import java.util.List;

public class Parser {
    public List<GitCommit> parse(final List<String> lines) {
        final List<GitCommit> logEntries = new LinkedList<GitCommit>();
        String currentSha = null;
        String currentAuthor = null;
        String currentDate = null;
        List<CommitFile> currentCommitFiles = new LinkedList<CommitFile>();
        List<String> currentMessage = new LinkedList<String>();
        for (String line : lines) {
            if(line.startsWith("commit")) {
                if(currentSha != null) {
                    logEntries.add(new GitCommit(currentSha, currentAuthor, currentDate, currentCommitFiles, currentMessage));
                    currentAuthor = null;
                    currentDate = null;
                    currentCommitFiles = new LinkedList<CommitFile>();
                    currentMessage = new LinkedList<String>();
                }

                currentSha = line.substring("commit ".length()).trim();
            }
            else if(line.startsWith("Author:")) {
                currentAuthor = line.substring("Author: ".length()).trim();
            }
            else if(line.startsWith("Date:")) {
                currentDate = line.substring("Date: ".length()).trim();
            }
            else if(line.startsWith("A") || line.startsWith("M") || line.startsWith("D")) {
                currentCommitFiles.add(new CommitFile(line));
            } else if(line.trim().length() > 0) {
                currentMessage.add(line.trim());
            }
        }

        if(currentSha != null) {
            logEntries.add(new GitCommit(currentSha, currentAuthor, currentDate, currentCommitFiles, currentMessage));
        }

        return logEntries;
    }
}
