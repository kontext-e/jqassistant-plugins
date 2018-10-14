package de.kontext_e.jqassistant.plugin.javaparser.scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;

class MyNode {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyNode.class);

	private final Store store;
	private final Integer id;

	MyNode(final Store store, final Integer id) {
		this.store = store;
		this.id = id;
	}

	private long getId() {
		return id;
	}

	void createRelationshipTo(final MyNode otherNode, final String type) {
		LOGGER.debug("Create relationship " + type);
		store.executeQuery("MATCH (me), (other) WHERE id(me) = "+id+" AND id(other) = "+otherNode.getId()+" CREATE (me)-[r:"+type+"]->(other)");
	}

	void setProperty(final String key, final Object value) {
		LOGGER.debug("SET " + key + " = '" + value + "'");
		String escaped = value.toString()
				.replaceAll("\\\\","\\\\\\\\")
				.replaceAll("'","\\\\'")
				;
		final String query = "MATCH (n) WHERE id(n) = " + id + " SET n." + key + " = '" + escaped + "'";
		try {
			store.executeQuery(query);
		} catch (Exception e) {
			LOGGER.warn("Error in query "+query+ " with value "+value);
		}
	}
}
