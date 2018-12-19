package de.kontext_e.jqassistant.plugin.cpp.scanner;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.cpp.store.descriptor.CppClangAstDescriptor;

/**
 * alias stresc='sed -r "s/\x1B\[[0-9]{1,2};?(;[0-9]{1,2}){,2}m//g"'
 * stresc Foo.ast > Foo.stripped.ast
 */
@ScannerPlugin.Requires(FileDescriptor.class)
public class CppScannerPlugin extends AbstractScannerPlugin<FileResource, CppClangAstDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CppScannerPlugin.class);

    @Override
    public boolean accepts(final FileResource item, final String path, final Scope scope) throws IOException {
        try {
            boolean accepted = path.endsWith(".ast");
            if(accepted) {
                LOGGER.info("Accepted "+path);
            }
            return accepted;
        } catch (NullPointerException e) {
            // could do a lengthy null check at beginning or do it the short dirty way
            return false;
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }
    }

    @Override
    public CppClangAstDescriptor scan(final FileResource item, final String path, final Scope scope, final Scanner scanner) {
        try {
            LOGGER.debug("Cpp scans path "+path);
            Store store = scanner.getContext().getStore();
            FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
            final CppClangAstDescriptor cppClangAstDescriptor = store.addDescriptorType(fileDescriptor, CppClangAstDescriptor.class);
            new CppAstParser().readStream(store, item.createStream());
            return cppClangAstDescriptor;
        } catch (Exception e) {
            LOGGER.error("Error while checking scanning path "+path+": "+e, e);
            return null;
        }
    }
}
