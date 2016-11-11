package de.kontext_e.jqassistant.plugin.plaintext.store.descriptor;

import com.buschmais.xo.neo4j.api.annotation.Label;

@Label("Line")
public interface PlaintextLineDescriptor extends PlaintextDescriptor {

    int getLineNumber();
    void setLineNumber(int lineNumber);

    String getText();
    void setText(String text);

}
