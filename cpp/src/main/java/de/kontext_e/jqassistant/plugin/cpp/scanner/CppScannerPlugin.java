package de.kontext_e.jqassistant.plugin.cpp.scanner;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.cpp.store.descriptor.CppDescriptor;

public class CppScannerPlugin extends AbstractScannerPlugin<FileResource, CppDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CppScannerPlugin.class);

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        return path.endsWith(".ast");
    }

    @Override
    public CppDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) throws IOException {
        LOGGER.debug("Cpp scans path "+path);
        final CppDescriptor checkstyleDescriptor = scanner.getContext().getStore().create(CppDescriptor.class);
        checkstyleDescriptor.setFileName(path);
        return checkstyleDescriptor;
    }
}
