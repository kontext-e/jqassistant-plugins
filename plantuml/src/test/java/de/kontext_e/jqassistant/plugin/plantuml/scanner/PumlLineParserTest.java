package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlFileDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PumlLineParserTest {
    private PumlLineParser pumlLineParser;
    private Store mockStore = mock(Store.class);
    private PlantUmlFileDescriptor plantUmlFileDescriptor = mock(PlantUmlFileDescriptor.class);

    @Before
    public void setUp() {
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);
    }

    @Test
    public void thatAPackagesIsRecognized() throws Exception {
        final PlantUmlPackageDescriptor mockDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockDescriptor);

        pumlLineParser.parseLine("package de.kontext_e.jqassistant.plugin.plantuml.scanner {}");
        verify(mockDescriptor).setFullQualifiedName("de.kontext_e.jqassistant.plugin.plantuml.scanner");

        pumlLineParser.parseLine("package root {}");
        verify(mockDescriptor).setFullQualifiedName("root");

        pumlLineParser.parseLine("package without.space{}");
        verify(mockDescriptor).setFullQualifiedName("without.space");

        pumlLineParser.parseLine("package next.line");
        verify(mockDescriptor).setFullQualifiedName("next.line");
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


    @Test
    public void thatEmbeddedPlantUMLInAsciidocIsRead() throws Exception {
        final String asciidoc = "=== Level 1\n" +
                                "\n" +
                                "\n" +
                                "The following diagram shows the main building blocks of the system and their interdependencies:\n" +
                                "\n" +
                                "[\"plantuml\",\"MainBuildingBlocks.png\",\"png\"]\n" +
                                "----\n" +
                                "package de.kontext_e.project.domain #ffffff {\n" +
                                "}\n" +
                                "package de.kontext_e.project.services #ffffff {\n" +
                                "}\n" +
                                "\n" +
                                "de.kontext_e.project.services --> de.kontext_e.project.domain\n" +
                                "\n" +
                                "-----\n" +
                                "\n" +
                                "Comments regarding structure and interdependencies at Level 1:\n";
        String[] lines = asciidoc.split("\\n");
        Assert.assertThat(lines.length, Matchers.greaterThan(1));
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.IGNORING);
        final PlantUmlPackageDescriptor mockDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockDescriptor);
        final Set<PlantUmlPackageDescriptor> mockSet = mock(Set.class);
        when(mockDescriptor.getMayDependOnPackages()).thenReturn(mockSet);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }


        verify(mockSet).add(mockDescriptor);
    }

}
