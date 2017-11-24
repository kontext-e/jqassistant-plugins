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
    public void thatPumlFileIsRead() throws Exception {
        final String puml = "@startuml\n" +
                            "\n" +
                            "skinparam packageStyle rect\n" +
                            "\n" +
                            "package scanner {}\n" +
                            "package store {\n" +
                            "    package descriptor {}\n" +
                            "}\n" +
                            "\n" +
                            "scanner --> descriptor\n" +
                            "\n" +
                            "@enduml\n";
        String[] lines = puml.split("\\n");
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);
        final PlantUmlPackageDescriptor mockDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockDescriptor);
        final Set<PlantUmlPackageDescriptor> mockSet = mock(Set.class);
        when(mockDescriptor.getMayDependOnPackages()).thenReturn(mockSet);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }

        verify(mockSet).add(mockDescriptor);
    }

    @Test
    public void thatComponentPumlFileIsRead() throws Exception {
        final String puml = "@startuml\n" +
                            "\n" +
                            "skinparam componentStyle uml2\n" +
                            "skinparam component {\n" +
                            "  FontSize 13\n" +
                            "  FontName Arial\n" +
                            "  FontColor #99c0d0\n" +
                            "  BorderColor black\n" +
                            "  BackgroundColor #42788e\n" +
                            "  ArrowFontName Impact\n" +
                            "  ArrowColor #42788e\n" +
                            "  ArrowFontColor #42788e\n" +
                            "\n" +
                            "\n" +
                            "  BackgroundColor<<UI>> Red\n" +
                            "  BorderColor<<UI>> #FF6655\n" +
                            "}\n" +
                            "\n" +
                            "component TestComponent1 [\n" +
                            "     <size:20><b><u>TestComponent1</u></b></size>\n" +
                            "]\n" +
                            "component TestComponent2 [\n" +
                            "     <size:20><b><u>TestComponent2</u></b></size>\n" +
                            "]\n" +
                            "\n" +
                            "TestComponent1 --> TestComponent2\n" +
                            "\n" +
                            "@enduml\n";
        String[] lines = puml.split("\\n");
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);
        final PlantUmlPackageDescriptor mockDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockDescriptor);
        final Set<PlantUmlPackageDescriptor> mockSet = mock(Set.class);
        when(mockDescriptor.getMayDependOnPackages()).thenReturn(mockSet);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }

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

    @Test
    public void thatEmbeddedPlantUMLInAsciidocIsRead2() throws Exception {
        final String asciidoc = "=== Level 1\n" +
                                "\n" +
                                "\n" +
                                "The following diagram shows the main building blocks of the system and their interdependencies:\n" +
                                "\n" +
                                "[\"plantuml\",\"MainBuildingBlocks\",\"png\"]\n" +
                                "-----\n" +
                                "component Domain\n" +
                                "component [JSF UI]\n" +
                                "\n" +
                                "[JSF UI] --> Domain\n" +
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
