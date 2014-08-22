package com.visulytic.bibviz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;

import com.visulytic.bibviz.model.graphdb.Edge;
import com.visulytic.bibviz.model.graphdb.GraphDB;
import com.visulytic.bibviz.model.graphdb.Node;
import com.visulytic.bibviz.model.lucene.LuceneDB;
import com.visulytic.bibviz.project.BibVizProject;
import com.visulytic.bibviz.view.LoadDBEvent;

import edu.uci.ics.jung.graph.Graph;

public class BibVizModel {

	private LuceneDB luceneDB;
	private GraphDB graphDB;
	private List<LoadDBListener> loadDBListeners = new ArrayList<>();

	public BibVizModel(BibVizProject project) {
		luceneDB = new LuceneDB(project.getLuceneDir());
		graphDB = new GraphDB(project.getN4JDir());
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				close();
			}
		}));
	}

	public void addEntry(Key k, BibTeXEntry bibEntry) {
		long entryID = graphDB.addPublication(k, bibEntry);
		luceneDB.addEntry(k, bibEntry, entryID);
	}

	public Set<Long> search(String queryText, List<String> fields) {
		Set<Long> node4jIds = luceneDB.search(queryText, fields);
		return node4jIds;
	}

	public void close() {
		luceneDB.close();
		graphDB.close();
	}

	public void loadDatabase(BibTeXDatabase db) {
		for (Key k : db.getEntries().keySet()) {
			BibTeXEntry e = db.getEntries().get(k);
			addEntry(k, e);
		}

		LoadDBEvent event = new LoadDBEvent(this);

		fire(event);
	}

	public void addLoadDBListener(LoadDBListener listener) {
		loadDBListeners.add(listener);
	}

	private void fire(LoadDBEvent event) {
		for (LoadDBListener listener : loadDBListeners) {
			listener.dbLoaded(event);
		}
	}

	public Graph<Node, Edge> getGraph() {
		return graphDB.getGraph();
	}
}
