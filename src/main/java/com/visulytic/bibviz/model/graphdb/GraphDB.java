package com.visulytic.bibviz.model.graphdb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.visulytic.bibviz.view.helper.NodeJSONGenerator;

public class GraphDB {
	private GraphDatabaseService publicationDB;
	private ExecutionEngine executionEngine;
	private Transaction currentTransaction;
	private File n4jDir;

	public GraphDB(File n4jDir) {
		this.n4jDir = n4jDir;
		init();
	}

	public void init() {
		publicationDB = new GraphDatabaseFactory().newEmbeddedDatabase(n4jDir
				.getAbsolutePath());

		executionEngine = new ExecutionEngine(publicationDB);
	}

	public long addEntry(Key k, BibTeXEntry bibEntry) {
		Transaction tx = publicationDB.beginTx();
		Node publicationNode = publicationDB.createNode();
		publicationNode.addLabel(DynamicLabel.label("Publication"));

		if (k != null) {
			String entryId = k.getValue();
			System.out.println(entryId);
			publicationNode.setProperty("entryId", entryId);
		}

		Value authorsValue = bibEntry.getField(BibTeXEntry.KEY_AUTHOR);
		if (authorsValue != null) {
			String authorString = authorsValue.toUserString();

			// split the user string using and

			String[] authors = authorString.split("and");

			publicationNode.setProperty("authors", authors);
		}

		Value titleValue = bibEntry.getField(BibTeXEntry.KEY_TITLE);
		if (titleValue != null) {
			String title = titleValue.toUserString();

			publicationNode.setProperty("title", title);
		}

		Value abstractValue = bibEntry.getField(new Key("abstract"));
		if (abstractValue != null) {
			String abstractString = abstractValue.toUserString();

			publicationNode.setProperty("abstract", abstractString);
		}

		tx.success();
		tx.close();

		return publicationNode.getId();

	}

	public List<Node> getAllNodes() {

		List<Node> nodes = new ArrayList<Node>();
		Transaction tx = publicationDB.beginTx();
		String queryString = "match (n:Publication) return n";
		ExecutionResult result = executionEngine.execute(queryString);
		ResourceIterator<Object> resourceIterator = result.columnAs("n");
		while (resourceIterator.hasNext()) {
			Node n = (Node) resourceIterator.next();
			nodes.add(n);
		}
		tx.success();
		tx.close();

		return nodes;
	}

	public String getAllNodesAsJson() {
		Transaction tx = publicationDB.beginTx();
		List<Node> allNodes = getAllNodes();
		String json = NodeJSONGenerator.getJSON(allNodes);
		tx.success();
		tx.close();
		return json;
	}

	public void beginTransaction() {
		if (currentTransaction != null)
			throw new RuntimeException(
					"A transaction is open. Please close the current before creating another");
		else
			this.currentTransaction = publicationDB.beginTx();
	}

	public void endTransaction() {
		this.currentTransaction.close();
		this.currentTransaction = null;
	}

	public void close() {
		publicationDB.shutdown();
	}

}
