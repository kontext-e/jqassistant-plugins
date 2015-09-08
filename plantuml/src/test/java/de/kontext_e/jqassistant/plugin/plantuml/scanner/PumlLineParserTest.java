package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import org.junit.Before;
import org.junit.Test;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PumlLineParserTest {
    private PumlLineParser pumlLineParser;
    private Store mockStore = mock(Store.class);

    @Before
    public void setUp() {
        pumlLineParser = new PumlLineParser(mockStore);
    }

    @Test
    public void thatAPackagesIsRecognized() throws Exception {
        final String line = "package de.kontext_e.jqassistant.plugin.plantuml.scanner {";
        final PlantUmlPackageDescriptor mockDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockDescriptor);

        pumlLineParser.parseLine(line);

        verify(mockDescriptor).setFullQualifiedName("de.kontext_e.jqassistant.plugin.plantuml.scanner");
    }


}
