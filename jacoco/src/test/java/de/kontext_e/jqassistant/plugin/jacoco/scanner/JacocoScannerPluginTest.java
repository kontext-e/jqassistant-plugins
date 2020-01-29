package de.kontext_e.jqassistant.plugin.jacoco.scanner;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JacocoScannerPluginTest {

    @Test
    public void aValidPathShouldBeAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = "/jacoco/test/jacocoTestReport.xml";

        final boolean accepted = plugin.acceptsPath(path);

        assertTrue("A valid path should be accepted", accepted);
    }

    @Test
    public void aValidPathAsRootShouldBeAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = "/jacocoTestReport.xml";

        final boolean accepted = plugin.acceptsPath(path);

        assertTrue("A valid path should be accepted", accepted);
    }

    @Test
    public void anInvValidPathAsRootShouldNotBeAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = "/jacocoTestReport.foo";

        final boolean accepted = plugin.acceptsPath(path);

        assertFalse("An invalid path should not be accepted", accepted);
    }

    @Test
    public void aValidSingleFileNameShouldBeAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = "jacocoTestReport.xml";

        final boolean accepted = plugin.acceptsPath(path);

        assertTrue("A valid path should be accepted", accepted);
    }

    @Test
    public void aValidParentDirectoryShouldBeAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = "/jacoco/report.xml";

        final boolean accepted = plugin.acceptsPath(path);

        assertTrue("A valid parent directory should be accepted", accepted);
    }

    @Test
    public void anInvalidPathGetsNotAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = "/pmd/report.xml";

        final boolean accepted = plugin.acceptsPath(path);

        assertFalse("An invalid parent directory should not be accepted", accepted);
    }

    @Test
    public void pathNullShouldNotBeAccepted() throws IOException {
        JacocoScannerPlugin plugin = new JacocoScannerPlugin();
        String path = null;

        final boolean accepted = plugin.accepts(null, path, null);

        assertFalse("Path NULL should not be accepted", accepted);
    }


}
