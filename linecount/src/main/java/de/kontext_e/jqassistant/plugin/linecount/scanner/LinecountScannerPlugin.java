package de.kontext_e.jqassistant.plugin.linecount.scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.linecount.store.descriptor.LinecountDescriptor;

import static java.util.Arrays.asList;

public class LinecountScannerPlugin extends AbstractScannerPlugin<FileResource, LinecountDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinecountScannerPlugin.class);
    public static final String SUFFIXES = "jqassistant.plugin.linecount.suffixes";
    private final Set<String> acceptedSuffixes = new HashSet<>();

    public LinecountScannerPlugin() {
        acceptedSuffixes.addAll(asList("java", "xml", "html", "xhtml", "js", "gradle"));
    }

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        if(path.lastIndexOf(".") <= 0) return false;
        String suffix = path.substring(path.lastIndexOf(".") + 1);
        return acceptedSuffixes.contains(suffix.toLowerCase());
    }

    @Override
    public LinecountDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        Store store = scanner.getContext().getStore();
        final LinecountDescriptor linecountDescriptor = store.create(LinecountDescriptor.class);
        linecountDescriptor.setName(path);
        linecountDescriptor.setFileName(path);

        int lines = 0;
        try(BufferedReader reader = new BufferedReader(new FileReader(item.getFile().getAbsolutePath()))) {
            while (reader.readLine() != null) lines++;
        }
        linecountDescriptor.setLinecount(lines);

        return linecountDescriptor;
    }

    @Override
    protected void configure() {
        super.configure();

        Map<String, Object> properties = getProperties();
        final String suffixes = (String) properties.get(SUFFIXES);
        if(suffixes != null) {
            acceptSuffixes(suffixes);
        }
        if(System.getProperty(SUFFIXES) != null) {
            acceptSuffixes(System.getProperty(SUFFIXES));
        }
        LOGGER.debug("Linecount plugin accepts following suffixes: "+getAcceptedSuffixes());

    }

    protected void acceptSuffixes(final String suffixes) {
        String normalizedString = suffixes.replaceAll("[,;:]", " ").toLowerCase();
        acceptedSuffixes.clear();
        acceptedSuffixes.addAll(asList(normalizedString.split(" ")));
        acceptedSuffixes.remove("");
    }

    public Set<String> getAcceptedSuffixes() {
        return acceptedSuffixes;
    }
}
