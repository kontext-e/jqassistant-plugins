package de.kontext_e.jqassistant.plugin.cpp.scanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.cpp.store.descriptor.ClassDescriptor;

public class CppAstParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(CppAstParser.class);

    public void readStream(final Store store, final InputStream stream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try {
            while(reader.ready()) {
                parseLine(store, reader.readLine());
            }
        } catch (IOException e) {
            LOGGER.warn("Exception while reading AST file, could not read all lines; reason: "+e);
        }

    }

    private void parseLine(final Store store, final String line) {
        String normalizedLine = line.trim();
        if(normalizedLine.startsWith("CXXRecordDecl")) {
            parseCxxRecordDecl(store, normalizedLine);
        }
    }

    private void parseCxxRecordDecl(final Store store, final String normalizedLine) {
        if(normalizedLine.endsWith("definition") && normalizedLine.contains(" class ")) {
            ClassDescriptor classDescriptor = store.create(ClassDescriptor.class);
        }
    }
}
