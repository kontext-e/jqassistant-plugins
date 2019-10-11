package de.kontext_e.jqassistant.plugin.asciidoc.scanner;

import java.io.IOException;
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
import de.kontext_e.jqassistant.plugin.asciidoc.store.descriptor.AsciidocFileDescriptor;

import static java.util.Arrays.asList;

@ScannerPlugin.Requires(FileDescriptor.class)
public class AsciidocFileScannerPlugin extends AbstractScannerPlugin<FileResource, AsciidocFileDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsciidocFileScannerPlugin.class);
    private static final String JQASSISTANT_PLUGIN_ASCIIDOC_SUFFIXES = "jqassistant.plugin.asciidoc.suffixes";

    private static List<String> suffixes = asList("asciidoc", "adoc");

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        try {
            int beginIndex = path.lastIndexOf(".");
            if(beginIndex > 0) {
                final String suffix = path.substring(beginIndex + 1).toLowerCase();

                boolean accepted = suffixes.contains(suffix);
                if(accepted) {
                    LOGGER.info("Asciidoc accepted path "+path);
                }

                return accepted;
            }

            return false;
        } catch (NullPointerException e) {
            // could do a lengthy null check at beginning or do it the short dirty way
            return false;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    @Override
    public AsciidocFileDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        try {
            final Store store = scanner.getContext().getStore();
            FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
            final AsciidocFileDescriptor asciidocFileDescriptor  = store.addDescriptorType(fileDescriptor, AsciidocFileDescriptor.class);
            asciidocFileDescriptor.setFileName(path);

            new AsciidocImporter(item.getFile(), store, 20).importDocument(asciidocFileDescriptor);

            return asciidocFileDescriptor;
        } catch (IOException e) {
            LOGGER.error("Error while checking scanning path "+path+": "+e, e);
            return null;
        }
    }

    @Override
    protected void configure() {
        super.configure();

        if(getProperties().containsKey(JQASSISTANT_PLUGIN_ASCIIDOC_SUFFIXES)) {
            suffixes = new ArrayList<>();
            String serializedSuffixes = (String) getProperties().get(JQASSISTANT_PLUGIN_ASCIIDOC_SUFFIXES);
            for (String suffix : serializedSuffixes.split(",")) {
                suffixes.add(suffix.toLowerCase().trim());
            }
            LOGGER.info("Asciidoc accepts suffixes "+suffixes);
        }
    }

}
