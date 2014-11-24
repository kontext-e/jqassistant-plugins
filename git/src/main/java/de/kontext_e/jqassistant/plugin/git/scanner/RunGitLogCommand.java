package de.kontext_e.jqassistant.plugin.git.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.asList;

public class RunGitLogCommand {
    public static List<String> runGitLog(final String pathToGitCommand, final String pathToGitProject, final String range) throws IOException, InterruptedException {
        final List<String> lines = new LinkedList<String>();
        final List<String> errorLines = new LinkedList<String>();
        List<String> args = new ArrayList<>(asList(pathToGitCommand, "--git-dir="+ pathToGitProject, "log", "--name-status", "--date=iso"));
        if(range != null) args.add(range);
        final Process process = Runtime.getRuntime().exec(args.toArray(new String[args.size()]));
        inputReader(lines, process.getInputStream());
        inputReader(errorLines, process.getErrorStream());
        process.waitFor();

        if(errorLines.size() > 0) {
            System.err.println("errors: " + errorLines);
            throw new RuntimeException("Error on executing git command");
        }

        return lines;
    }

    private static void inputReader(final List<String> lines, final InputStream inputStream) throws IOException {
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                            String line;
                            while ((line = reader.readLine()) != null) {
                                lines.add(line);
                            }
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
    }
}
