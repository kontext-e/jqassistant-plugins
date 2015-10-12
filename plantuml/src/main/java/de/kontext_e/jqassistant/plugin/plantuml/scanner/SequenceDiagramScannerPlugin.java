package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlDescriptor;

public class SequenceDiagramScannerPlugin extends AbstractScannerPlugin<FileResource, PlantUmlDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SequenceDiagramScannerPlugin.class);

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        boolean accepted = path.endsWith(".puml") || path.endsWith(".adoc");
        if(accepted) {
            LOGGER.info("PlantUML accepted path "+path);
        }
        return accepted;
    }

    @Override
    public PlantUmlDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        final Store store = scanner.getContext().getStore();
        final PlantUmlDescriptor plantUmlDescriptor = store.create(PlantUmlDescriptor.class);
        plantUmlDescriptor.setFileName(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(item.createStream()))) {
            final PumlLineParser pumlLineParser = new PumlLineParser(store, path.endsWith(".puml") ? ParsingState.ACCEPTING : ParsingState.IGNORING);
            String line;
            while ((line = reader.readLine()) != null) {
                pumlLineParser.parseLine(line);
            }
        }

        return plantUmlDescriptor;
    }


}
