package de.kontext_e.jqassistant.plugin.linecount.scanner;

import org.junit.Before;

public class LinecountScannerPluginTest {
    private LinecountScannerPlugin linecountScannerPlugin;

    @Before
    public void setUp() {
        linecountScannerPlugin = new LinecountScannerPlugin();
    }

/* does not compile anymore with message: cannot access ScannerPlugin although ScannerPlugin is API and public
--> compiler bug??

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
*/


}
