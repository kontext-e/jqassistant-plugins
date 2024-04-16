package de.kontext_e.jqassistant.plugin.linecount.scanner;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LinecountScannerPluginTest {
    private LinecountScannerPlugin linecountScannerPlugin;

    @Before
    public void setUp() {
        linecountScannerPlugin = new LinecountScannerPlugin();
    }

    @Test
    public void thatConfiguredSuffixesWereSaved() {
        linecountScannerPlugin.acceptSuffixes("java, xml; cs:gradle adoc");

        assertThat(linecountScannerPlugin.getAcceptedSuffixes().containsAll(List.of("java","xml","cs","gradle","adoc")))
                .withFailMessage("Unexpected suffix list: "+linecountScannerPlugin.getAcceptedSuffixes())
                .isTrue();
    }

    @Test
    public void thatConfiguredSuffixesWereAccepted() {
        linecountScannerPlugin.acceptSuffixes("java, xml");

        assertThat(linecountScannerPlugin.accepts(null, "File.java", null)).withFailMessage("java suffix is configured and should be accepted").isTrue();
        assertThat(linecountScannerPlugin.accepts(null, "File.xml", null)).withFailMessage("xml suffix is configured and should be accepted").isTrue();
        assertThat(linecountScannerPlugin.accepts(null, "File.XML", null)).withFailMessage("suffix should not be case sensitive").isTrue();
    }


}
