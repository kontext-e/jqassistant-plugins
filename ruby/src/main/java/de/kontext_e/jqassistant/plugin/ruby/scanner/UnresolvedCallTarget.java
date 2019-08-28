package de.kontext_e.jqassistant.plugin.ruby.scanner;

import de.kontext_e.jqassistant.plugin.ruby.store.descriptor.MethodDescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class UnresolvedCallTarget {
    private final String receiverFqn;
    private final String normativeSignature;
    private final List<MethodDescriptor> callSources = new ArrayList<>();

    UnresolvedCallTarget(String receiverFqn, String normativeSignature) {
        this.receiverFqn = receiverFqn;
        // a "new" calls the target "initialize" method
        this.normativeSignature = "new".equals(normativeSignature) ? "initialize" : normativeSignature;
    }

    public String getReceiverFqn() {
        return receiverFqn;
    }

    public String getNormativeSignature() {
        return normativeSignature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnresolvedCallTarget that = (UnresolvedCallTarget) o;
        return Objects.equals(receiverFqn, that.receiverFqn) &&
                Objects.equals(normativeSignature, that.normativeSignature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiverFqn, normativeSignature);
    }

    @Override
    public String toString() {
        return "UnresolvedCallTarget{" +
                "receiverFqn='" + receiverFqn + '\'' +
                ", normativeSignature='" + normativeSignature + '\'' +
                ", callSources=" + callSources +
                '}';
    }


    void addCallSource(MethodDescriptor declaredMethod) {
        callSources.add(declaredMethod);
    }

    public List<MethodDescriptor> getCallSources() {
        return Collections.unmodifiableList(callSources);
    }
}
