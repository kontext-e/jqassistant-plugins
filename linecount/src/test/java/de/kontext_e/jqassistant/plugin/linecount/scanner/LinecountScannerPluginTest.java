package de.kontext_e.jqassistant.plugin.linecount.scanner;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class LinecountScannerPluginTest {
    private LinecountScannerPlugin linecountScannerPlugin;

    @Before
    public void setUp() {
        linecountScannerPlugin = new LinecountScannerPlugin();
    }

    @Test
    public void thatConfiguredSuffixesWereSaved() throws Exception {
        linecountScannerPlugin.acceptSuffixes("java, xml; cs:gradle adoc");

        assertThat("Unexpected suffix list: "+linecountScannerPlugin.getAcceptedSuffixes(),
                   linecountScannerPlugin.getAcceptedSuffixes(),
                   containsInAnyOrder("java","xml","cs","gradle","adoc"));
    }

    @Test
    public void thatConfiguredSuffixesWereAccepted() throws Exception {
        linecountScannerPlugin.acceptSuffixes("java, xml");

        assertThat("java suffix is configured and should be accepted", linecountScannerPlugin.accepts(null, "File.java", null), Matchers.is(true));
        assertThat("xml suffix is configured and should be accepted", linecountScannerPlugin.accepts(null, "File.xml", null), Matchers.is(true));
        assertThat("suffix should not be case sensitive", linecountScannerPlugin.accepts(null, "File.XML", null), Matchers.is(true));
    }


}
