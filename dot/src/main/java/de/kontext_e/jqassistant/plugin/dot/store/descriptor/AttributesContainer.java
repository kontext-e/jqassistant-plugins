package de.kontext_e.jqassistant.plugin.dot.store.descriptor;

import com.buschmais.xo.api.annotation.Abstract;
import com.buschmais.xo.api.annotation.ImplementedBy;
import com.buschmais.xo.api.proxy.ProxyMethod;
import com.buschmais.xo.neo4j.embedded.impl.model.EmbeddedNeo4jPropertyContainer;

@Abstract
public interface AttributesContainer {

    @ImplementedBy(DotRelationDescriptor.SetAttributeMethod.class)
    String setAttribute(String name, String value, String type);

    class SetAttributeMethod implements ProxyMethod<EmbeddedNeo4jPropertyContainer> {

        @Override
        public Object invoke(EmbeddedNeo4jPropertyContainer node, Object instance, Object[] args) {
            node.setProperty((String) args[0], args[1]);
            node.setProperty("type", args[2]);
            return args[1];
        }
    }

}
