package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlFileDescriptor;
import de.kontext_e.jqassistant.plugin.plantuml.store.descriptor.PlantUmlPackageDescriptor;

class PumlLineParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(PumlLineParser.class);

    private final Store store;
    private final Map<String, PlantUmlPackageDescriptor> mappingFromFqnToPackage = new HashMap<>();
    private final PlantUmlFileDescriptor plantUmlFileDescriptor;
    private ParsingState parsingState = ParsingState.ACCEPTING;

    public PumlLineParser(final Store store, final PlantUmlFileDescriptor plantUmlFileDescriptor, final ParsingState parsingState) {
        this.store = store;
        this.plantUmlFileDescriptor = plantUmlFileDescriptor;
        this.parsingState = parsingState;
    }

    protected void parseLine(final String line) {
        if(line == null) return;

        String normalizedLine = line.trim().toLowerCase();

        if(parsingState == ParsingState.IGNORING && normalizedLine.startsWith("[\"plantuml\"")) {
            parsingState = ParsingState.PLANTUMLFOUND;
        }
        if(parsingState == ParsingState.ACCEPTING && normalizedLine.startsWith("----")) {
            parsingState = ParsingState.IGNORING;
        }
        if(parsingState == ParsingState.PLANTUMLFOUND && normalizedLine.startsWith("----")) {
            parsingState = ParsingState.ACCEPTING;
        }

        if(parsingState == ParsingState.ACCEPTING) {
            if (normalizedLine.startsWith("package ")) {
                PlantUmlPackageDescriptor packageNode = createPackageNode(line);
                plantUmlFileDescriptor.getPlantUmlElements().add(packageNode);
            }

            if (!normalizedLine.startsWith("-") && normalizedLine.contains("-")) {
                createRelation(line);
            }
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
            LOGGER.warn("Syntax error: unknown lhs package "+lhs+ " of line "+line);
            return;
        }
        if(!mappingFromFqnToPackage.containsKey(rhs)) {
            LOGGER.warn("Syntax error: unknown rhs package "+rhs+ " of line "+line);
            return;
        }

        mappingFromFqnToPackage.get(lhs).getMayDependOnPackages().add(mappingFromFqnToPackage.get(rhs));
    }

    protected PlantUmlPackageDescriptor createPackageNode(final String line) {
        int lengthOfPackageLiteral = "package ".length();
        int endIndex = line.indexOf(" ", lengthOfPackageLiteral);
        if(endIndex < 0) {
            endIndex = line.indexOf("{", lengthOfPackageLiteral);
        }
        if(endIndex < 0) {
            endIndex = line.length();
        }
        final String name = line.substring(lengthOfPackageLiteral, endIndex).trim();
        PlantUmlPackageDescriptor packageDescriptor = store.create(PlantUmlPackageDescriptor.class);
        packageDescriptor.setFullQualifiedName(name);
        mappingFromFqnToPackage.put(name, packageDescriptor);
        return packageDescriptor;
    }
}
