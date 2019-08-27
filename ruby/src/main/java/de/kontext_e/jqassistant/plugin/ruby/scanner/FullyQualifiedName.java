package de.kontext_e.jqassistant.plugin.ruby.scanner;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

class FullyQualifiedName {
    private final List<String> parts;

    FullyQualifiedName(String fqn) {
        parts = asList(fqn.split("::"));
    }

    FullyQualifiedName(List<String> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return "FullyQualifiedName{" +
                "parts=" + parts +
                '}';
    }

    FullyQualifiedName replaceLastBy(String superClassName) {
        final List<String> withoutLast = new ArrayList<>(parts.subList(0, parts.size() - 1));
        withoutLast.add(superClassName);
        return new FullyQualifiedName(withoutLast);
    }

    public String asString() {
        return String.join("::", parts);
    }
}
