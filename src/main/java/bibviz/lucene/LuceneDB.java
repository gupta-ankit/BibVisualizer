package bibviz.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;

public class LuceneDB {
	private static final String LUCENE_DIR = "publications_lucene";
	private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_36);
	private IndexWriterConfig wc = new IndexWriterConfig(Version.LUCENE_36,
			analyzer);

	private Directory dir;
	private IndexWriter indexWriter;
	private QueryParser parser;

	public void init() {
		File luceneDir = null;
		try {
			luceneDir = new File(LUCENE_DIR);
			FileUtils.deleteDirectory(luceneDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			dir = FSDirectory.open(luceneDir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			indexWriter = new IndexWriter(dir, wc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			public void run() {
				try {
					indexWriter.close();
					dir.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}));

	}

	public void addEntry(Key k, BibTeXEntry bibEntry, long entryID) {
		Document doc = new Document();

		Field neo4jID = new Field("n4jID", "" + entryID, Field.Store.YES,
				Index.NOT_ANALYZED);
		doc.add(neo4jID);

		Field idField = new Field("pubID", k.getValue(), Field.Store.YES,
				Index.NOT_ANALYZED);
		doc.add(idField);

		Key[] noStoreKeys = new Key[] { BibTeXEntry.KEY_TITLE,
				new Key("abstract") };
		for (Key iKey : noStoreKeys) {
			Value value = bibEntry.getField(iKey);
			String string = value.toUserString();
			Field f = new Field(iKey.getValue(), string, Field.Store.NO,
					Index.ANALYZED);
			doc.add(f);
		}

		try {
			indexWriter.addDocument(doc);
			indexWriter.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method searches the lucene database and returns a list of neo4j ids
	 * for the corresponding documents.
	 * 
	 * @param queryText
	 * @param fields
	 * @return
	 */
	public Set<Integer> search(String queryText, List<String> fields) {
		System.out.println("Searching Lucene " + queryText + " - " + fields);
		Set<Integer> nodeIds = new HashSet<Integer>();

		for (String field : fields) {
			nodeIds.addAll(search(queryText, field));
		}

		return nodeIds;
	}

	private List<Integer> search(String queryText, String field) {
		IndexReader reader;
		List<Integer> list2return = new ArrayList<Integer>();
		try {
			reader = IndexReader.open(dir);

			IndexSearcher searcher = new IndexSearcher(reader);
			parser = new QueryParser(Version.LUCENE_36, field, analyzer);

			Query query = parser.parse(queryText);
			TopDocs search = searcher.search(query, Integer.MAX_VALUE);

			for (int i = 0; i < search.scoreDocs.length; i++) {
				int docId = search.scoreDocs[i].doc;
				Document doc = searcher.doc(docId);
				list2return.add(Integer.parseInt(doc.get("n4jID")));
			}

			searcher.close();
			reader.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list2return;
	}
}
