package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;

class PumlLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumlLineParser.class);

    private final Store store;
    private final Map<String, PlantUmlPackageDescriptor> mappingFromFqnToPackage = new HashMap<>();

    public PumlLineParser(final Store store) {
        this.store = store;
    }

    protected void parseLine(final String line) {
        if(line == null) return;

        String normalizedLine = line.trim().toLowerCase();

        if(normalizedLine.startsWith("package ")) {
            createPackageNode(line);
        }

        if(normalizedLine.contains("-")) {
            createRelation(line);
        }
    }

    private void createRelation(final String line) {
        boolean swap = false;
        if(line.contains("<")) {
            swap = true;
        }

        String packageNamesOnly = line.replaceAll("[-<>]", " ");
        String lhs = packageNamesOnly.substring(0, packageNamesOnly.indexOf(" ")).trim();
        String rhs = packageNamesOnly.substring(packageNamesOnly.indexOf(" ")).trim();

        if(swap) {
            String tmp = lhs;
            lhs = rhs;
            rhs = tmp;
        }

        if(!mappingFromFqnToPackage.containsKey(lhs)) {
            LOGGER.warn("Syntax error: unknown package "+lhs);
            return;
        }
        if(!mappingFromFqnToPackage.containsKey(rhs)) {
            LOGGER.warn("Syntax error: unknown package "+rhs);
            return;
        }

        mappingFromFqnToPackage.get(lhs).getMayDependOnPackages().add(mappingFromFqnToPackage.get(rhs));
    }

    protected PlantUmlPackageDescriptor createPackageNode(final String line) {
        final String name = line.substring("package ".length(), line.indexOf("{")).trim();
        PlantUmlPackageDescriptor packageDescriptor = store.create(PlantUmlPackageDescriptor.class);
        packageDescriptor.setFullQualifiedName(name);
        mappingFromFqnToPackage.put(name, packageDescriptor);
        return packageDescriptor;
    }
}
