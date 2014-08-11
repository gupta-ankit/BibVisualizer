package bibviz.model;

import java.util.List;
import java.util.Set;

import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.neo4j.graphdb.Node;

import bibviz.graphdb.GraphDB;
import bibviz.lucene.LuceneDB;

public class BibVizModel {

	private LuceneDB luceneDB;
	private GraphDB graphDB;

	public BibVizModel() {
		luceneDB = new LuceneDB();
		luceneDB.init();
		graphDB = new GraphDB();
		graphDB.init();
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
	
	public void endTransaction(){
		graphDB.endTransaction();
	}
}
