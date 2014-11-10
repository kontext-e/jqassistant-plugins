package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlSequenceDiagramDescriptor;

public class SequenceDiagramScannerPlugin extends AbstractScannerPlugin<FileResource, PlantUmlSequenceDiagramDescriptor> {

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        return path.endsWith(".puml");
    }

    @Override
    public PlantUmlSequenceDiagramDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        final PlantUmlSequenceDiagramDescriptor plantUmlSequenceDiagramDescriptor = scanner.getContext().getStore().create(PlantUmlSequenceDiagramDescriptor.class);
        plantUmlSequenceDiagramDescriptor.setFileName(path);

        BufferedReader reader = new BufferedReader(new InputStreamReader(item.createStream()));
        String line;
        boolean inDiagram = false;
        while((line = reader.readLine()) != null) {
            if(line.contains("@startuml")) {
                inDiagram = true;
                continue;
            }
            if(line.contains("@enduml")) {
                inDiagram = false;
                continue;
            }
            if(inDiagram) {

            }
        }

        return plantUmlSequenceDiagramDescriptor;
    }


}
