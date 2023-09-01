package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlFileDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlParticipantDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramMessageDescriptor;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SequenceDiagramTest {
    private PumlLineParser pumlLineParser;
    private Store mockStore = mock(Store.class);
    private PlantUmlFileDescriptor plantUmlFileDescriptor = mock(PlantUmlFileDescriptor.class);

    private PlantUmlSequenceDiagramDescriptor mockDescriptor;
    private PlantUmlParticipantDescriptor mockPlantUmlParticipantDescriptor;
    private PlantUmlSequenceDiagramMessageDescriptor mockPlantUmlSequenceDiagramMessageDescriptor;

    @Before
    public void setUp() {
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor);
        mockDescriptor = mock(PlantUmlSequenceDiagramDescriptor.class);
        mockPlantUmlParticipantDescriptor = mock(PlantUmlParticipantDescriptor.class);
        mockPlantUmlSequenceDiagramMessageDescriptor = mock(PlantUmlSequenceDiagramMessageDescriptor.class);

        when(mockStore.create(PlantUmlSequenceDiagramDescriptor.class)).thenReturn(mockDescriptor);
        when(mockStore.create(PlantUmlParticipantDescriptor.class)).thenReturn(mockPlantUmlParticipantDescriptor);
        when(mockStore.create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class)))
                .thenReturn(mockPlantUmlSequenceDiagramMessageDescriptor);

        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor);
    }

    @Test
    public void thatSimpleSequenceDiagramIsRead() {
        final String puml = "@startuml\n" +
                            "Alice -> Bob: Authentication Request\n" +
                            "Bob --> Alice: Authentication Response\n" +
                            "\n" +
                            "Alice -> Bob: Another authentication Request\n" +
                            "Alice <-- Bob: another authentication Response\n" +
                            "@enduml\n";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(4)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(4)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithParticipantDeclarations() {
        final String puml = "@startuml\n" +
                            "participant Foo0\n" +
                            "actor Foo1 as foofoo\n" +
                            "boundary Foo2\n" +
                            "control Foo3\n" +
                            "entity Foo4\n" +
                            "database Foo5\n" +
                            "collections Foo6\n" +
                            "Foo1 -> Foo2 : To boundary\n" +
                            "Foo1 -> Foo3 : To control\n" +
                            "Foo1 -> Foo4 : To entity\n" +
                            "Foo1 -> Foo5 : To database\n" +
                            "Foo1 -> Foo6 : To collections\n" +
                            "\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        // 7 participants + 1 for renaming using "as"
        verify(mockStore, times(8)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(5)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(5)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithColorAndLongName() {
        final String puml = "@startuml\n" +
                            "actor Bob #red\n" +
                            "' The only difference between actor\n" +
                            "'and participant is the drawing\n" +
                            "participant Alice\n" +
                            "participant \"I have a really\\nlong name\" as L #99FF99\n" +
                            "/' You can also declare:\n" +
                            "   participant L as \"I have a really\\nlong name\"  #99FF99\n" +
                            "  '/\n" +
                            "\n" +
                            "Alice->Bob: Authentication Request\n" +
                            "Bob->Alice: Authentication Response\n" +
                            "Bob->L: Log transaction\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(3)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(3)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(3)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithNotAlphanumericalNames() {
        final String puml = "@startuml\n" +
                            "Alice -> \"Bob()\" : Hello\n" +
                            "\"Bob()\" -> \"This is very\\nlong\" as Long\n" +
                            "' You can also declare:\n" +
                            "' \"Bob()\" -> Long as \"This is very\\nlong\"\n" +
                            "Long --> \"Bob()\" : ok\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(3)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(3)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(3)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithMessageToSelf() {
        final String puml = "@startuml\n" +
                            "Alice->Alice: This is a signal to self.\\nIt also demonstrates\\nmultiline \\ntext\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(1)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(1)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(1)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithDifferentArrowStyles() {
        final String puml = "@startuml\n" +
                            "Bob ->x Alice\n" +
                            "Bob -> Alice\n" +
                            "Bob ->> Alice\n" +
                            "Bob -\\ Alice\n" +
                            "Bob \\\\- Alice\n" +
                            "Bob //-- Alice\n" +
                            "\n" +
                            "Bob ->o Alice\n" +
                            "Bob o\\\\-- Alice\n" +
                            "\n" +
                            "Bob <-> Alice\n" +
                            "Bob <->o Alice\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(10)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(10)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithDifferentArrowColors() {
        final String puml = "@startuml\n" +
                            "Bob -[#red]> Alice : hello\n" +
                            "Alice -[#0000FF]->Bob : ok\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(2)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(2)).setMessage(Mockito.anyString());
    }

    @Test
    public void sequenceDiagramWithMessageNumbers() {
        final String puml = "@startuml\n" +
                            "autonumber\n" +
                            "Bob -> Alice : Authentication Request\n" +
                            "Bob <- Alice : Authentication Response\n" +
                            "@enduml";

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(2)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(2)).setMessage(Mockito.anyString());
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(1)).setMessageNumber("<b>1</b>");
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(1)).setMessageNumber("<b>2</b>");
    }

}
