package de.kontext_e.jqassistant.plugin.javaparser.scanner;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.xo.api.CompositeObject;
import com.buschmais.xo.api.Query;

class MyGraphDatabaseService {
	private final Store store;

	MyGraphDatabaseService(final Store store) {
		this.store = store;
	}

	Node createNode(final Label... labels) {
		String labelsClause = "";
		for (Label label: labels) {
			labelsClause += ":" + label.name();
		}

		final Query.Result<Query.Result.CompositeRowObject> compositeRowObjects = store.executeQuery("CREATE (n" + labelsClause + ") RETURN n");
		final Query.Result.CompositeRowObject singleResult = compositeRowObjects.getSingleResult();
		CompositeObject compositeObject = singleResult.get("n", CompositeObject.class);
		Object idObject = compositeObject.getId();
		int id = ((Number)idObject).intValue();
		compositeRowObjects.close();
		return new MyNode(store, id);
	}
}
