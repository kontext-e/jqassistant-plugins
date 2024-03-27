package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.*;
import net.sourceforge.plantuml.*;
import net.sourceforge.plantuml.classdiagram.AbstractEntityDiagram;
import net.sourceforge.plantuml.core.Diagram;
import net.sourceforge.plantuml.error.PSystemError;
import net.sourceforge.plantuml.sequencediagram.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

class PlantUMLLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantUMLLineParser.class);

    private final Store store;
    private final PlantUmlFileDescriptor plantUmlFileDescriptor;
    private ParsingState parsingState;
    private StringBuilder lineBuffer = new StringBuilder();
    private String pictureFileName;
    private String pictureFileType;

    PlantUMLLineParser(final Store store, final PlantUmlFileDescriptor plantUmlFileDescriptor, final ParsingState parsingState) {
        this.store = store;
        this.plantUmlFileDescriptor = plantUmlFileDescriptor;
        this.parsingState = parsingState;
    }

    void parseLine(final String line) {
        if(line == null) return;

        String normalizedLine = line.trim().toLowerCase();

        // inlcudes cannot be handled because of working directory mismatch while scanning
        if(normalizedLine.startsWith("!include")) { return; }

        if(parsingState == ParsingState.IGNORING && normalizedLine.startsWith("[\"plantuml\"")) {
            parsingState = ParsingState.PLANTUMLFOUND;
            analyzeHeader(normalizedLine);
        }

        if(parsingState == ParsingState.ACCEPTING && (normalizedLine.startsWith("----") || normalizedLine.startsWith("@enduml"))) {
            parsingState = ParsingState.IGNORING;
            lineBuffer.append("\n").append("@enduml");
            storeDiagram();
            prepareScannerForNextDiagram();
        }

        if(parsingState == ParsingState.PLANTUMLFOUND && normalizedLine.startsWith("----")) {
            parsingState = ParsingState.ACCEPTING;
            startNewLineBuffer();
            return;
        }

        if(parsingState == ParsingState.ACCEPTING) {
            lineBuffer.append(normalizedLine).append("\n");
        }
    }

    private void startNewLineBuffer() {
        lineBuffer = new StringBuilder();
        lineBuffer.append("@startuml");
        lineBuffer.append("\n");
    }

    private void prepareScannerForNextDiagram() {
        pictureFileName = null;
        pictureFileType = null;
    }

    private void analyzeHeader(String normalizedLine) {
        final String[] parts = normalizedLine
                .replaceAll("\\[", "")
                .replaceAll("]", "")
                .replaceAll("\"", "")
                .replaceAll("\n", "")
                .split(",");
        if(parts.length >= 3) {
            pictureFileName = parts[1];
            pictureFileType = parts[2];
        }
    }

    private void storeDiagram() {
        SourceStringReader reader = new SourceStringReader(lineBuffer.toString());
        List<BlockUml> blocks = reader.getBlocks();
        if(blocks.isEmpty()) return;

        final Diagram diagram = blocks.get(0).getDiagram();
        if(diagram instanceof PSystemError) {
            PSystemError error = (PSystemError) diagram;
            LOGGER.warn("Error while parsing at position: "+error.getErrorsUml());
            return;
        }


        if(diagram instanceof SequenceDiagram) {
            SequenceDiagramAnalyzer sequenceDiagramAnalyzer = new SequenceDiagramAnalyzer(store, pictureFileName, pictureFileType);
            final PlantUmlDiagramDescriptor diagramDescriptor = sequenceDiagramAnalyzer.analyzeDiagram((SequenceDiagram) diagram);
            if (diagramDescriptor == null) return;

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
        }
        else if(diagram instanceof AbstractEntityDiagram) {
            AbstractEntityDiagramAnalyzer diagramAnalyzer = new AbstractEntityDiagramAnalyzer(store, pictureFileName, pictureFileType);
            final PlantUmlDiagramDescriptor diagramDescriptor = diagramAnalyzer.analyzeDiagram((AbstractEntityDiagram) diagram);
            if (diagramDescriptor == null) return;

            plantUmlFileDescriptor.getPlantUmlDiagrams().add(diagramDescriptor);
            setOldRelationsForCompatibility(diagramDescriptor);
        }
    }

    private void setOldRelationsForCompatibility(final PlantUmlDiagramDescriptor diagramDescriptor) {
        if(diagramDescriptor instanceof PlantUmlClassDiagramDescriptor) {
            PlantUmlClassDiagramDescriptor pucdd = (PlantUmlClassDiagramDescriptor) diagramDescriptor;
            pucdd.getPlantUmlGroups().forEach(g -> {
                if(g instanceof PlantUmlPackageDescriptor) {
                    PlantUmlPackageDescriptor pupd = (PlantUmlPackageDescriptor) g;
                    pupd.getLinkTargets().forEach(lt -> {
                        if(lt instanceof PlantUmlPackageDescriptor) {
                            pupd.getMayDependOnPackages().add((PlantUmlPackageDescriptor) lt);
                        }
                    });
                    pupd.getChildGroups().forEach(cg -> {
                        if(cg instanceof PlantUmlPackageDescriptor) {
                            pupd.getContainedPackages().add((PlantUmlPackageDescriptor) cg);
                        }
                    });
                }
            });
        }
    }
}
