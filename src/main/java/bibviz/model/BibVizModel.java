package bibviz.model;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.neo4j.graphdb.Node;
import org.neo4j.helpers.Listeners;

import bibviz.graphdb.GraphDB;
import bibviz.lucene.LuceneDB;
import bibviz.project.BibVizProject;
import bibviz.visualization.LoadDBEvent;

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
		long entryID = graphDB.addEntry(k, bibEntry);
		luceneDB.addEntry(k, bibEntry, entryID);
	}

	public List<Node> getAllNodes() {
		return graphDB.getAllNodes();
	}

	public Set<Integer> search(String queryText, List<String> fields) {
		System.out.println("Searching Lucene " + queryText + " - " + fields);
		Set<Integer> node4jIds = luceneDB.search(queryText, fields);
		return node4jIds;
	}

	public void beginTransaction() {
		graphDB.beginTransaction();
	}

	public void endTransaction() {
		graphDB.endTransaction();
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
}
