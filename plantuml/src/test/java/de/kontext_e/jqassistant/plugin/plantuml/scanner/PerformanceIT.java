package de.kontext_e.jqassistant.plugin.plantuml.scanner;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import static org.neo4j.helpers.collection.IteratorUtil.loop;

public class PerformanceIT {

    private static final String DB_PATH = ".";

    public static void main(String[] args)  {
        new PerformanceIT().run();
    }

    private void run() {
        GraphDatabaseService graphDb = null;
        try {
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
            registerShutdownHook( graphDb );

            importDb(graphDb);
            queryDb(graphDb);
            cleanDb(graphDb);
        } finally {
            if(graphDb != null) {
                graphDb.shutdown();
            }
        }
    }

    private void cleanDb(final GraphDatabaseService graphDb) {
        try ( Transaction tx = graphDb.beginTx() )
        {
            for ( Node node : loop(graphDb.findNodes(DynamicLabel.label("Package"))))
            {
                for (Relationship rel : node.getRelationships()) {
                    rel.delete();
                }
                node.delete();
            }
            tx.success();
        }
    }

    private void queryDb(final GraphDatabaseService graphDb) {
        final long start = System.currentTimeMillis();
        Result result = graphDb.execute("MATCH\n" +
                                        "                (p1:PlantUml:Package)-[:MAY_DEPEND_ON]->(p2:PlantUml:Package),\n" +
                                        "                (p3:Java:Package)-[:DEPENDS_ON]->(p4:Java:Package)\n" +
                                        "            WHERE\n" +
                                        "                p1.fqn = p4.fqn\n" +
                                        "                AND p2.fqn = p3.fqn\n" +
                                        "            RETURN\n" +
                                        "                p3.fqn + \"-->\" + p4.fqn AS WrongDirection;");
        final long end = System.currentTimeMillis();
        System.out.println(result.resultAsString());
        System.out.println("Query took "+ (end-start)+"ms");
    }

    private void importDb(final GraphDatabaseService graphDb) {
        Label packageLabel = DynamicLabel.label("Package" );
        Label javaLabel = DynamicLabel.label("Java" );
        Label plantUmlLabel = DynamicLabel.label("PlantUml" );
        try ( Transaction tx = graphDb.beginTx() )
        {
            Node firstNode;
            Node secondNode;

            // create X Java dependencies
            for(int i = 0; i < 100000; i++) {
                firstNode = graphDb.createNode(packageLabel, javaLabel);
                firstNode.setProperty( "fqn", "p1_"+i );
                secondNode = graphDb.createNode(packageLabel, javaLabel);
                secondNode.setProperty( "fqn", "p2_"+i );
                firstNode.createRelationshipTo(secondNode, RelTypes.DEPENDS_ON);
            }

            // create X PlantUml dependencies correct direction
            for(int i = 0; i < 1000; i++) {
                firstNode = graphDb.createNode(packageLabel, plantUmlLabel);
                firstNode.setProperty( "fqn", "p1_"+i );
                secondNode = graphDb.createNode(packageLabel, plantUmlLabel);
                secondNode.setProperty( "fqn", "p2_"+i );
                firstNode.createRelationshipTo(secondNode, RelTypes.MAY_DEPEND_ON);
            }
            // create X PlantUml dependencies wrong direction
            for(int i = 0; i < 10; i++) {
                firstNode = graphDb.createNode(packageLabel, plantUmlLabel);
                firstNode.setProperty( "fqn", "p2_"+i );
                secondNode = graphDb.createNode(packageLabel, plantUmlLabel);
                secondNode.setProperty( "fqn", "p1_"+i );
                firstNode.createRelationshipTo(secondNode, RelTypes.MAY_DEPEND_ON);
            }
            tx.success();
        }

/*
        try ( Transaction tx = graphDb.beginTx() )
        {
            graphDb.schema().indexFor(packageLabel )
                    .on( "fqn" )
                    .create();
            tx.success();
        }
*/
    }

    private enum RelTypes implements RelationshipType
    {
        MAY_DEPEND_ON, DEPENDS_ON
    }

    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
}
