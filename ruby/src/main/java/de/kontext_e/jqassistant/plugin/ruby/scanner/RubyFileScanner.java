package de.kontext_e.jqassistant.plugin.ruby.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.RubyFileDescriptor;
import org.jrubyparser.CompatVersion;
import org.jrubyparser.Parser;
import org.jrubyparser.ast.Node;
import org.jrubyparser.parser.ParserConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;

class RubyFileScanner {
    private AddDescriptorVisitor addDescriptorVisitor;

    RubyFileScanner(RubyFileDescriptor rubyFileDescriptor, Store store) {
        addDescriptorVisitor = new AddDescriptorVisitor(rubyFileDescriptor, store);
    }

    void scan(InputStream inputStream) {
        Parser rubyParser = new Parser();
        CompatVersion version = CompatVersion.RUBY2_0; // FIXME make configurable
        ParserConfiguration config = new ParserConfiguration(0, version);
        final Node parse = rubyParser.parse("<code>", new InputStreamReader(inputStream), config);

        traverseNode(parse);
    }

    private void traverseNode(Node node) {
        node.accept(addDescriptorVisitor);
        for (Node childNode : node.childNodes()) {
            traverseNode(childNode);
        }
    }

}
