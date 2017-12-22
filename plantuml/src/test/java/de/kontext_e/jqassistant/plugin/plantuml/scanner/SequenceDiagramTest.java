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

    @Before
    public void setUp() {
        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);
    }

    @Test
    public void thatSequenceDiagramIsRead() {
        final String puml = "@startuml\n" +
                            "Alice -> Bob: Authentication Request\n" +
                            "Bob --> Alice: Authentication Response\n" +
                            "\n" +
                            "Alice -> Bob: Another authentication Request\n" +
                            "Alice <-- Bob: another authentication Response\n" +
                            "@enduml\n";

        pumlLineParser = new PumlLineParser(mockStore, plantUmlFileDescriptor, ParsingState.ACCEPTING);

        final PlantUmlSequenceDiagramDescriptor mockDescriptor = mock(PlantUmlSequenceDiagramDescriptor.class);
        when(mockStore.create(PlantUmlSequenceDiagramDescriptor.class)).thenReturn(mockDescriptor);
        final PlantUmlParticipantDescriptor mockPlantUmlParticipantDescriptor = mock(PlantUmlParticipantDescriptor.class);
        when(mockStore.create(PlantUmlParticipantDescriptor.class)).thenReturn(mockPlantUmlParticipantDescriptor);
        final PlantUmlSequenceDiagramMessageDescriptor mockPlantUmlSequenceDiagramMessageDescriptor = mock(PlantUmlSequenceDiagramMessageDescriptor.class);
        when(mockStore.create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class)))
                .thenReturn(mockPlantUmlSequenceDiagramMessageDescriptor);

        asList(puml.split("\\n")).forEach(line -> pumlLineParser.parseLine(line));

        verify(mockStore).create(PlantUmlSequenceDiagramDescriptor.class);
        verify(mockStore, times(2)).create(PlantUmlParticipantDescriptor.class);
        verify(mockStore, times(4)).create(Mockito.any(PlantUmlParticipantDescriptor.class), Mockito.eq(PlantUmlSequenceDiagramMessageDescriptor.class), Mockito.any(PlantUmlParticipantDescriptor.class));
        verify(mockPlantUmlSequenceDiagramMessageDescriptor, times(4)).setMessage(Mockito.anyString());
    }

}
