package de.kontext_e.jqassistant.plugin.ruby.scanner;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.RubyFileDescriptor;
import org.jruby.Ruby;
import org.jruby.ast.Node;
import org.jruby.parser.Parser;
import org.jruby.parser.ParserConfiguration;
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

            //new RubyFileScanner(rubyFileDescriptor, scanner.getContext().getStore()).scan(item.createStream());

            Parser rubyParser = new Parser(Ruby.getGlobalRuntime());
            ParserConfiguration config = new ParserConfiguration(Ruby.getGlobalRuntime(), 0, true, true, false);
            final Node parse = rubyParser.parse("<code>", item.createStream(), null, config);
            traverseNode(parse, null, rubyFileDescriptor, scanner.getContext().getStore());


            return rubyFileDescriptor;
        } catch (Exception e) {
            LOGGER.warn("Error while scanning a Ruby file: "+e, e);
            return null;
        }
    }

    private void traverseNode(Node node, JRubyVisitor parentVisitor, RubyFileDescriptor rubyFileDescriptor, Store store) {
        if(node == null) return;

        final JRubyVisitor visitor = new JRubyVisitor(rubyFileDescriptor, store, node, parentVisitor);
        node.accept(visitor);
        for (Node childNode : node.childNodes()) {
            traverseNode(childNode, visitor, rubyFileDescriptor, store);
        }
    }

}
