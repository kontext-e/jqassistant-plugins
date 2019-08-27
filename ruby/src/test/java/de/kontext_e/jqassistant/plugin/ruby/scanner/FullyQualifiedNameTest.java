package de.kontext_e.jqassistant.plugin.ruby.scanner;

import org.junit.Test;

import static org.junit.Assert.*;

public class FullyQualifiedNameTest {

    @Test
    public void replaceWithThreeParts() {
        final FullyQualifiedName fullyQualifiedName = new FullyQualifiedName("a::b::x");

        final FullyQualifiedName replaced = fullyQualifiedName.replaceLastBy("c");

        assertEquals("Wrong replace last", "a::b::c", replaced.asString());
    }

    @Test
    public void replaceWithTwoParts() {
        final FullyQualifiedName fullyQualifiedName = new FullyQualifiedName("a::b");

        final FullyQualifiedName replaced = fullyQualifiedName.replaceLastBy("c");

        assertEquals("Wrong replace last", "a::c", replaced.asString());
    }

    @Test
    public void replaceWithOnePart() {
        final FullyQualifiedName fullyQualifiedName = new FullyQualifiedName("a");

        final FullyQualifiedName replaced = fullyQualifiedName.replaceLastBy("c");

        assertEquals("Wrong replace last", "c", replaced.asString());
    }

    @Test
    public void replaceWithNoPart() {
        final FullyQualifiedName fullyQualifiedName = new FullyQualifiedName("");

        final FullyQualifiedName replaced = fullyQualifiedName.replaceLastBy("c");

        assertEquals("Wrong replace last", "c", replaced.asString());
    }

}

