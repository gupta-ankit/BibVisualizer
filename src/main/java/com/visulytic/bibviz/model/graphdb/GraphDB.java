package com.visulytic.bibviz.model.graphdb;

import java.io.File;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

public class GraphDB {
	// Relationships
	private static final String WROTE_RELATIONSHIP = "wrote";
	private static final String IS_FIRST_AUTHOR = "is_first_author";

	private int id = 0;

	// Additional Constants
	private static final String GRAPH_FILE_NAME = "graph.ser";

	private File graphDir;
	private Graph<Node, Edge> graph;

	public GraphDB(File graphDir) {
		this.graphDir = graphDir;
		graphDir.mkdir();
		init();
	}

	public void init() {

		// load the graph id one exits
		File graphFile = new File(graphDir, GRAPH_FILE_NAME);
		if (graphFile.exists()) {
			graph = GraphDBUtils.readGraphObjectFromFile(graphFile);
		} else {
			graph = new DirectedSparseMultigraph<>();
		}
	}

	public long addPublication(Key k, BibTeXEntry bibEntry) {

		PublicationNode publicationNode = new PublicationNode();
		publicationNode.setId(id++);
		graph.addVertex(publicationNode);
		// publicationNode.

		if (k != null) {
			String entryId = k.getValue();
			publicationNode.setProperty("entryId", entryId);
		}

		Value authorsValue = bibEntry.getField(BibTeXEntry.KEY_AUTHOR);
		if (authorsValue != null) {
			String authorString = authorsValue.toUserString();

			// split the user string using and

			String[] authors = authorString.split("and");

			for (String author : authors) {
				AuthorNode authorNode = new AuthorNode();
				authorNode.setId(id++);
				graph.addVertex(authorNode);
				authorNode.setLabel(author);
				if (author == authors[0]) {
					// if the first node
					Edge edge = new DefaultEdge();
					edge.setType(IS_FIRST_AUTHOR);
					graph.addEdge(edge, publicationNode, authorNode);
				}
				Edge edge = new DefaultEdge();
				edge.setType(WROTE_RELATIONSHIP);
				graph.addEdge(edge, publicationNode, authorNode);
			}
		}

		Value titleValue = bibEntry.getField(BibTeXEntry.KEY_TITLE);
		if (titleValue != null) {
			String title = titleValue.toUserString();
			publicationNode.setLabel(title);
			publicationNode.setProperty("title", title);
		}

		Value abstractValue = bibEntry.getField(new Key("abstract"));
		if (abstractValue != null) {
			String abstractString = abstractValue.toUserString();
			publicationNode.setProperty("abstract", abstractString);
		}

		return publicationNode.getId();
	}

	public void close() {
		File graphFile = new File(graphDir, GRAPH_FILE_NAME);
		GraphDBUtils.writeGraphToFile(graphFile, graph);
	}

	public Graph<Node, Edge> getGraph() {
		return graph;
	}
}
