package de.kontext_e.jqassistant.plugin.plaintext.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.plaintext.store.descriptor.PlaintextFileDescriptor;

import static java.util.Arrays.asList;

public class PlaintextFileScannerPlugin extends AbstractScannerPlugin<FileResource, PlaintextFileDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaintextFileScannerPlugin.class);
    public static final String JQASSISTANT_PLUGIN_PLAINTEXT_SUFFIXES = "jqassistant.plugin.plaintext.suffixes";

    private static List<String> suffixes = asList("puml", "adoc", "txt", "hpp", "cpp", "c", "h");


    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        int beginIndex = path.lastIndexOf(".");
        if(beginIndex > 0) {
            final String suffix = path.substring(beginIndex + 1).toLowerCase();

            boolean accepted = suffixes.contains(suffix);
            if(accepted) {
                LOGGER.info("Plaintext accepted path "+path);
            }

            return accepted;
        }

        return false;
    }

    @Override
    public PlaintextFileDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        final Store store = scanner.getContext().getStore();
        final PlaintextFileDescriptor plaintextFileDescriptor = store.create(PlaintextFileDescriptor.class);
        plaintextFileDescriptor.setFileName(path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(item.createStream()))) {
            final PlaintextLineParser pumlLineParser = new PlaintextLineParser(store, plaintextFileDescriptor);
            String line;
            while ((line = reader.readLine()) != null) {
                pumlLineParser.parseLine(line);
            }
        }

        return plaintextFileDescriptor;
    }

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_PLAINTEXT_SUFFIXES)) {
            suffixes = new ArrayList<>();
            String serializedSuffixes = (String) getProperties().get(JQASSISTANT_PLUGIN_PLAINTEXT_SUFFIXES);
            for (String suffix : serializedSuffixes.split(",")) {
                suffixes.add(suffix.toLowerCase().trim());
            }
        }

        LOGGER.info(String.format("Plaintext plugin looks for files with suffixes '%s'", suffixes.toString()));
    }

}
