package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlClassDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlDescriptionDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlFileDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlLeafDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlParticipantDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramMessageDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlStateDiagramDescriptor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
    public void thatPackagePumlFileIsRead() {
        final String puml = "@startuml\n" +
                            "\n" +
                            "skinparam packageStyle rect\n" +
                            "\n" +
                            "class SomeClass \n" +
                            "package scanner {}\n" +
                            "package store {\n" +
                            "    package descriptor {\n" +
                            "       interface PlantUmlDescriptor\n"+
                            "    }\n" +
                            "}\n" +
                            "\n" +
                            "scanner --> descriptor\n" +
                            "SomeClass --> PlantUmlDescriptor\n" +
                            "\n" +
                            "@enduml\n";
        String[] lines = puml.split("\\n");
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);

        final PlantUmlClassDiagramDescriptor mockPlantUmlClassDiagramDescriptor = mock(PlantUmlClassDiagramDescriptor.class);
        when(mockStore.create(PlantUmlClassDiagramDescriptor.class)).thenReturn(mockPlantUmlClassDiagramDescriptor);
        final PlantUmlPackageDescriptor mockPackageDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockPackageDescriptor);
        final PlantUmlLeafDescriptor mockPlantUmlLeafDescriptor = mock(PlantUmlLeafDescriptor.class);
        when(mockStore.create(PlantUmlLeafDescriptor.class)).thenReturn(mockPlantUmlLeafDescriptor);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }

        verify(mockStore).create(PlantUmlClassDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlLeafDescriptor.class);
        verify(mockStore, times(3)).create(PlantUmlPackageDescriptor.class);
        verify(mockPackageDescriptor, times(1)).getChildGroups();
        verify(mockPackageDescriptor, times(1)).getLeafs();
        verify(mockPlantUmlClassDiagramDescriptor).setType("CLASSDIAGRAM");
        verify(mockPlantUmlLeafDescriptor, times(1)).setType("INTERFACE");
        verify(mockPlantUmlLeafDescriptor, times(1)).setType("CLASS");
        verify(mockPackageDescriptor, times(1)).getLinkTargets();
        verify(mockPlantUmlLeafDescriptor, times(1)).getLinkTargets();
    }

    @Test
    public void thatComponentPumlFileIsRead() {
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
                            "component TestComponent1 <<UI>> <<abstract>> [\n" +
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

        final PlantUmlClassDiagramDescriptor mockDescriptionDiagramDescriptor = mock(PlantUmlClassDiagramDescriptor.class);
        when(mockStore.create(PlantUmlClassDiagramDescriptor.class)).thenReturn(mockDescriptionDiagramDescriptor);
        final PlantUmlLeafDescriptor mockPlantUmlLeafDescriptor = mock(PlantUmlLeafDescriptor.class);
        when(mockStore.create(PlantUmlLeafDescriptor.class)).thenReturn(mockPlantUmlLeafDescriptor);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }

        verify(mockStore).create(PlantUmlClassDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlLeafDescriptor.class);
		verify(mockDescriptionDiagramDescriptor).setType("CLASSDIAGRAM");
        verify(mockPlantUmlLeafDescriptor, times(2)).setType("DESCRIPTION");
        verify(mockPlantUmlLeafDescriptor, times(1)).getLinkTargets();
        verify(mockPlantUmlLeafDescriptor, times(1)).setStereotype("«ui»«abstract»");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("testcomponent1");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("testcomponent2");
    }

    @Test
    public void thatStateDiagramIsRead() {
        final String puml = "@startuml\n" +
                               "\n" +
                               "[*] --> State1\n" +
                               "State1 --> [*]\n" +
                               "State1 : this is a string\n" +
                               "State1 : this is another string\n" +
                               "\n" +
                               "State1 -> State2\n" +
                               "State2 --> [*]\n" +
                               "\n" +
                               "@enduml";

        String[] lines = puml.split("\\n");
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);

        final PlantUmlStateDiagramDescriptor mockDescriptor = mock(PlantUmlStateDiagramDescriptor.class);
        when(mockStore.create(PlantUmlStateDiagramDescriptor.class)).thenReturn(mockDescriptor);
        final PlantUmlLeafDescriptor mockPlantUmlLeafDescriptor = mock(PlantUmlLeafDescriptor.class);
        when(mockStore.create(PlantUmlLeafDescriptor.class)).thenReturn(mockPlantUmlLeafDescriptor);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }

        verify(mockStore).create(PlantUmlStateDiagramDescriptor.class);
        verify(mockStore, times(4)).create(PlantUmlLeafDescriptor.class);
		verify(mockDescriptor).setType("STATEDIAGRAM");
        verify(mockPlantUmlLeafDescriptor, times(1)).setType("CIRCLE_START");
        verify(mockPlantUmlLeafDescriptor, times(2)).setType("STATE");
        verify(mockPlantUmlLeafDescriptor, times(1)).setType("CIRCLE_END");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("*start");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("state1");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("state2");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("*end");
        verify(mockPlantUmlLeafDescriptor, times(4)).getLinkTargets();
    }

	@Test
	public void thatSequenceDiagramIsRead() {
		final String puml = "@startuml\n" +
							"autonumber\n" +
							"participant de.kontext_e.spikes.trace_to_plantuml.application.Controller <<Controller>>\n" +
							"de.kontext_e.spikes.trace_to_plantuml.application.Controller -> de.kontext_e.spikes.trace_to_plantuml.application.Boundary : loadEntity([1])\n" +
							"de.kontext_e.spikes.trace_to_plantuml.application.Boundary -> de.kontext_e.spikes.trace_to_plantuml.application.Repository : readEntity([1])\n" +
							"de.kontext_e.spikes.trace_to_plantuml.application.Repository -> de.kontext_e.spikes.trace_to_plantuml.application.Boundary : throws(NoSuchEntityException{id=1})\n" +
							"de.kontext_e.spikes.trace_to_plantuml.application.Boundary -> de.kontext_e.spikes.trace_to_plantuml.application.Controller : return([LoadEntityResult{results=NO_RESULT}])\n" +
							"de.kontext_e.spikes.trace_to_plantuml.application.Controller -> de.kontext_e.spikes.trace_to_plantuml.application.LoadEntityResult : getResults([])\n" +
							"de.kontext_e.spikes.trace_to_plantuml.application.LoadEntityResult -> de.kontext_e.spikes.trace_to_plantuml.application.Controller : return([NO_RESULT])\n" +
							"@enduml";

		String[] lines = puml.split("\\n");
		pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);

		final PlantUmlSequenceDiagramDescriptor mockDescriptor = mock(PlantUmlSequenceDiagramDescriptor.class);
		when(mockStore.create(PlantUmlSequenceDiagramDescriptor.class)).thenReturn(mockDescriptor);
		final PlantUmlParticipantDescriptor participantDescriptor = mock(PlantUmlParticipantDescriptor.class);
		when(mockStore.create(PlantUmlParticipantDescriptor.class)).thenReturn(participantDescriptor);
		final PlantUmlSequenceDiagramMessageDescriptor messageDescriptor = mock(PlantUmlSequenceDiagramMessageDescriptor.class);
		when(mockStore.create(participantDescriptor, PlantUmlSequenceDiagramMessageDescriptor.class, participantDescriptor)).thenReturn(messageDescriptor);

		for (String line : lines) {
			pumlLineParser.parseLine(line);
		}

		verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
		verify(mockDescriptor).setType("SEQUENCEDIAGRAM");
		verify(participantDescriptor, times(4)).setType("PARTICIPANT");
		verify(participantDescriptor).setName("de.kontext_e.spikes.trace_to_plantuml.application.controller");
		verify(participantDescriptor).setStereotype("«controller»");
		verify(messageDescriptor).setMessage("[loadentity([1])]");
		verify(messageDescriptor).setMessageNumber("<b>1</b>");
    }


	@Test
    public void thatEmbeddedPlantUMLInAsciidocIsRead() {
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

        final PlantUmlClassDiagramDescriptor mockPlantUmlClassDiagramDescriptor = mock(PlantUmlClassDiagramDescriptor.class);
        when(mockStore.create(PlantUmlClassDiagramDescriptor.class)).thenReturn(mockPlantUmlClassDiagramDescriptor);
        final PlantUmlPackageDescriptor mockPackageDescriptor = mock(PlantUmlPackageDescriptor.class);
        when(mockStore.create(PlantUmlPackageDescriptor.class)).thenReturn(mockPackageDescriptor);
        final PlantUmlLeafDescriptor mockPlantUmlLeafDescriptor = mock(PlantUmlLeafDescriptor.class);
        when(mockStore.create(PlantUmlLeafDescriptor.class)).thenReturn(mockPlantUmlLeafDescriptor);

        for (String line : lines) {
            pumlLineParser.parseLine(line);
        }

        verify(mockStore).create(PlantUmlClassDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlPackageDescriptor.class);
        verify(mockPackageDescriptor).getLinkTargets();
    }

    @Test
    public void thatEmbeddedPlantUMLInAsciidocIsRead2() {
        String[] lines = ("=== Level 1\n" +
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
                                        "Comments regarding structure and interdependencies at Level 1:\n")
                .split("\\n");
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.IGNORING);

        final PlantUmlDescriptionDiagramDescriptor mockDescriptionDiagramDescriptor = mock(PlantUmlDescriptionDiagramDescriptor.class);
        when(mockStore.create(PlantUmlDescriptionDiagramDescriptor.class)).thenReturn(mockDescriptionDiagramDescriptor);
        final PlantUmlLeafDescriptor mockPlantUmlLeafDescriptor = mock(PlantUmlLeafDescriptor.class);
        when(mockStore.create(PlantUmlLeafDescriptor.class)).thenReturn(mockPlantUmlLeafDescriptor);

        Arrays.stream(lines).forEach(line -> pumlLineParser.parseLine(line));


        verify(mockStore).create(PlantUmlDescriptionDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlLeafDescriptor.class);
        verify(mockPlantUmlLeafDescriptor, times(2)).setType("DESCRIPTION");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("domain");
        verify(mockPlantUmlLeafDescriptor, times(1)).setFullName("jsf ui");
        verify(mockPlantUmlLeafDescriptor, times(1)).getLinkTargets();
    }
}
