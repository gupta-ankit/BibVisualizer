package bibviz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXParser;
import org.jbibtex.ObjectResolutionException;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrException;

public class BibtexDBLoader {

	public static BibTeXDatabase ingest(File f) {
		BibTeXParser parser = null;
		try {
			parser = new BibTeXParser();
		} catch (TokenMgrException e1) {

			e1.printStackTrace();
		} catch (ParseException e1) {

			e1.printStackTrace();
		}
		FileReader reader = null;
		try {
			reader = new FileReader(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		BibTeXDatabase db = null;
		try {
			db = parser.parse(reader);
		} catch (ObjectResolutionException e1) {
			e1.printStackTrace();
		} catch (TokenMgrException e1) {
			e1.printStackTrace();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return db;
	}
}
