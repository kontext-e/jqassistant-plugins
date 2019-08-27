package de.kontext_e.jqassistant.plugin.ruby.scanner;

import java.util.Objects;

class UnresolvedIncludeTarget {
    private final String name;

    UnresolvedIncludeTarget(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnresolvedIncludeTarget that = (UnresolvedIncludeTarget) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UnresolvedIncludeTarget{" +
                "name='" + name + '\'' +
                '}';
    }

}
