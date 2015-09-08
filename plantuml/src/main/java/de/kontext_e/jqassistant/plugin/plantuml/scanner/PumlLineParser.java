package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;

class PumlLineParser {
    private final Store store;

    public PumlLineParser(final Store store) {
        this.store = store;
    }

    protected void parseLine(final String line) {
        if(line != null && line.trim().toLowerCase().startsWith("package ")) {
            createPackageNode(line);
        }
    }

    protected PlantUmlPackageDescriptor createPackageNode(final String line) {
        final String name = line.substring("package ".length(), line.indexOf("{")).trim();
        PlantUmlPackageDescriptor packageDescriptor = store.create(PlantUmlPackageDescriptor.class);
        packageDescriptor.setFullQualifiedName(name);
        return packageDescriptor;
    }
}
