package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.util.Set;
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

    @Test
    public void thatAPackageDependencyIsRecognized() throws Exception {
        final String line = "de.kontext_e.jqassistant.plugin.plantuml.scanner --> de.kontext_e.jqassistant.plugin.plantuml.store.descriptor";
        final PlantUmlPackageDescriptor mockDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockDescriptor);
        final Set<PlantUmlPackageDescriptor> mockSet = mock(Set.class);
        when(mockDescriptor.getMayDependOnPackages()).thenReturn(mockSet);
        pumlLineParser.parseLine("package de.kontext_e.jqassistant.plugin.plantuml.scanner {");
        pumlLineParser.parseLine("package de.kontext_e.jqassistant.plugin.plantuml.store.descriptor {");

        pumlLineParser.parseLine(line);

        verify(mockSet).add(mockDescriptor);
    }


}
