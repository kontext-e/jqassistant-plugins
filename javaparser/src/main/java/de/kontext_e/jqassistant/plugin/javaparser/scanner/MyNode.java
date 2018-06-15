package de.kontext_e.jqassistant.plugin.javaparser.scanner;

import java.util.Map;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Traverser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.buschmais.jqassistant.core.store.api.Store;

class MyNode implements Node {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyNode.class);

	private final Store store;
	private final Integer id;

	MyNode(final Store store, final Integer id) {
		this.store = store;
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void delete() {

	}

	@Override
	public Iterable<Relationship> getRelationships() {
		return null;
	}

	@Override
	public boolean hasRelationship() {
		return false;
	}

	@Override
	public Iterable<Relationship> getRelationships(final RelationshipType... types) {
		return null;
	}

	@Override
	public Iterable<Relationship> getRelationships(final Direction direction, final RelationshipType... types) {
		return null;
	}

	@Override
	public boolean hasRelationship(final RelationshipType... types) {
		return false;
	}

	@Override
	public boolean hasRelationship(final Direction direction, final RelationshipType... types) {
		return false;
	}

	@Override
	public Iterable<Relationship> getRelationships(final Direction dir) {
		return null;
	}

	@Override
	public boolean hasRelationship(final Direction dir) {
		return false;
	}

	@Override
	public Iterable<Relationship> getRelationships(final RelationshipType type, final Direction dir) {
		return null;
	}

	@Override
	public boolean hasRelationship(final RelationshipType type, final Direction dir) {
		return false;
	}

	@Override
	public Relationship getSingleRelationship(final RelationshipType type, final Direction dir) {
		return null;
	}

	@Override
	public Relationship createRelationshipTo(final Node otherNode, final RelationshipType type) {
		LOGGER.debug("Create relationship " + type.name());
		store.executeQuery("MATCH (me), (other) WHERE id(me) = "+id+" AND id(other) = "+otherNode.getId()+" CREATE (me)-[r:"+type.name()+"]->(other)");
		return null;
	}

	@Override
	public Iterable<RelationshipType> getRelationshipTypes() {
		return null;
	}

	@Override
	public int getDegree() {
		return 0;
	}

	@Override
	public int getDegree(final RelationshipType type) {
		return 0;
	}

	@Override
	public int getDegree(final Direction direction) {
		return 0;
	}

	@Override
	public int getDegree(final RelationshipType type, final Direction direction) {
		return 0;
	}

	@Override
	public Traverser traverse(final Traverser.Order traversalOrder, final StopEvaluator stopEvaluator, final ReturnableEvaluator returnableEvaluator, final RelationshipType relationshipType, final Direction direction) {
		return null;
	}

	@Override
	public Traverser traverse(final Traverser.Order traversalOrder, final StopEvaluator stopEvaluator, final ReturnableEvaluator returnableEvaluator, final RelationshipType firstRelationshipType, final Direction firstDirection, final RelationshipType secondRelationshipType, final Direction secondDirection) {
		return null;
	}

	@Override
	public Traverser traverse(final Traverser.Order traversalOrder, final StopEvaluator stopEvaluator, final ReturnableEvaluator returnableEvaluator, final Object... relationshipTypesAndDirections) {
		return null;
	}

	@Override
	public void addLabel(final Label label) {

	}

	@Override
	public void removeLabel(final Label label) {

	}

	@Override
	public boolean hasLabel(final Label label) {
		return false;
	}

	@Override
	public Iterable<Label> getLabels() {
		return null;
	}

	@Override
	public GraphDatabaseService getGraphDatabase() {
		return null;
	}

	@Override
	public boolean hasProperty(final String key) {
		return false;
	}

	@Override
	public Object getProperty(final String key) {
		return null;
	}

	@Override
	public Object getProperty(final String key, final Object defaultValue) {
		return null;
	}

	@Override
	public void setProperty(final String key, final Object value) {
		LOGGER.debug("SET " + key + " = '" + value + "'");
		store.executeQuery("MATCH (n) WHERE id(n) ="+id+" SET n."+key+" = '"+value+"'");
	}

	@Override
	public Object removeProperty(final String key) {
		return null;
	}

	@Override
	public Iterable<String> getPropertyKeys() {
		return null;
	}

	@Override
	public Map<String, Object> getProperties(final String... keys) {
		return null;
	}

	@Override
	public Map<String, Object> getAllProperties() {
		return null;
	}
}
