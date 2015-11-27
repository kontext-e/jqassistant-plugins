package de.kontext_e.jqassistant.plugin.git.scanner;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertThat;

/**
 * A Scanner based on Eclipse JGit.
 *
 * @author Gerd Aschemann - gerd@aschemann.net - @GerdAschemann
 *
 * @since 1.1.0
 */
// TODO This is not a real Unit test! Make gradle run it in some kind of integration test phase!!!
public class JGitScannerTest {
    @Test
    public void testScan () throws IOException {
        JGitScanner jGitScanner = new JGitScanner();
        List<GitCommit> commits = jGitScanner.scan ("../.git");

        assertThat (commits.size(), greaterThan (0));
    }
}
