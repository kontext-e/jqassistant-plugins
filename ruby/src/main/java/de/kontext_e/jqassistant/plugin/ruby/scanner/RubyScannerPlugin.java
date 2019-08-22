package de.kontext_e.jqassistant.plugin.ruby.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.RubyFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author jn4, Kontext E GmbH
 */
@ScannerPlugin.Requires(FileDescriptor.class)
public class RubyScannerPlugin extends AbstractScannerPlugin<FileResource, RubyFileDescriptor> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RubyScannerPlugin.class);

    @Override
    public boolean accepts(FileResource item, String path, Scope scope) {
        try {
            if(path.endsWith(".rb")) {
                LOGGER.info("Ruby scanner accepted file " + path);
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Error while checking path: "+e, e);
            return false;
        }

        return false;
    }

    @Override
    public RubyFileDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {
        try {
            FileDescriptor fileDescriptor = scanner.getContext().getCurrentDescriptor();
            final RubyFileDescriptor rubyFileDescriptor = scanner.getContext().getStore().addDescriptorType(fileDescriptor, RubyFileDescriptor.class);
            new RubyFileScanner(rubyFileDescriptor, scanner.getContext().getStore()).scan(item.createStream());
            return rubyFileDescriptor;
        } catch (Exception e) {
            LOGGER.warn("Error while scanning a Ruby file: "+e, e);
            return null;
        }
    }
}
