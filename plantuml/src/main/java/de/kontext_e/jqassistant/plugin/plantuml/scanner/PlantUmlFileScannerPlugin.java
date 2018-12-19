package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlFileDescriptor;

import static java.util.Arrays.asList;

@ScannerPlugin.Requires(FileDescriptor.class)
public class PlantUmlFileScannerPlugin extends AbstractScannerPlugin<FileResource, PlantUmlFileDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlantUmlFileScannerPlugin.class);
    private static final String JQASSISTANT_PLUGIN_PLANTUML_SUFFIXES = "jqassistant.plugin.plantuml.suffixes";

    private static List<String> suffixes = asList("puml", "adoc");

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) {
        try {
            int beginIndex = path.lastIndexOf(".");
            if(beginIndex > 0) {
                final String suffix = path.substring(beginIndex + 1).toLowerCase();

                boolean accepted = suffixes.contains(suffix);
                if(accepted) {
                    LOGGER.info("PlantUML accepted path "+path);
                }

                return accepted;
            }

            return false;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    @Override
    public PlantUmlFileDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        final Store store = scanner.getContext().getStore();
		FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
		final PlantUmlFileDescriptor plantUmlFileDescriptor = store.addDescriptorType(fileDescriptor, PlantUmlFileDescriptor.class);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(item.createStream()))) {
            final PumlLineParser pumlLineParser = new PumlLineParser(store, plantUmlFileDescriptor, path.endsWith(".puml") ? ParsingState.ACCEPTING : ParsingState.IGNORING);
            String line;
            while ((line = reader.readLine()) != null) {
                pumlLineParser.parseLine(line);
            }
        }

        return plantUmlFileDescriptor;
    }

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_PLANTUML_SUFFIXES)) {
            suffixes = new ArrayList<>();
            String serializedSuffixes = (String) getProperties().get(JQASSISTANT_PLUGIN_PLANTUML_SUFFIXES);
            for (String suffix : serializedSuffixes.split(",")) {
                suffixes.add(suffix.toLowerCase().trim());
            }
        }
    }
}
