package de.kontext_e.jqassistant.plugin.plaintext.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plaintext.store.descriptor.PlaintextFileDescriptor;
import de.kontext_e.jqassistant.plugin.plaintext.store.descriptor.PlaintextLineDescriptor;

class PlaintextLineParser {
    private final Store store;
    private final PlaintextFileDescriptor plaintextFileDescriptor;

    private int lineNumber = 1;

    PlaintextLineParser(final Store store, final PlaintextFileDescriptor plaintextFileDescriptor) {
        this.store = store;
        this.plaintextFileDescriptor = plaintextFileDescriptor;
    }

    void parseLine(final String line) {
        PlaintextLineDescriptor plaintextLineDescriptor = store.create(PlaintextLineDescriptor.class);
        plaintextLineDescriptor.setLineNumber(lineNumber++);
        plaintextLineDescriptor.setText(line);
        plaintextFileDescriptor.getLines().add(plaintextLineDescriptor);
    }
}
