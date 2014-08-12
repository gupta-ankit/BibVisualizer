package com.visulytic.bibviz.debug;

import static java.lang.System.out;

import java.util.Arrays;
import java.util.Map;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;
import org.jbibtex.Value;

public class DBPrinter {

	public static void printDB(BibTeXDatabase db, int numberOfEntries) {

		Map<Key, BibTeXEntry> entries = db.getEntries();

		int counter = 0;

		for (Key e : entries.keySet()) {
			
			out.println();
			out.println(e.getValue());
			
			Value authorsValue = entries.get(e)
					.getField(BibTeXEntry.KEY_AUTHOR);
			if (authorsValue != null) {
				String authorString = authorsValue.toUserString();

				// split the user string using and

				String[] authors = authorString.split("and");

				out.println(Arrays.toString(authors));
			}

			Value titleValue = entries.get(e).getField(BibTeXEntry.KEY_TITLE);
			if (titleValue != null) {
				String title = titleValue.toUserString();

				out.println(title);
			}

			Value abstractValue = entries.get(e).getField(new Key("abstract"));
			if (abstractValue != null) {
				String abstractString = abstractValue.toUserString();

				out.println(abstractString);
			}

			// out.println(entries.get(e).getFields().keySet());

			counter++;

			if (counter > numberOfEntries) {
				break;
			}
		}
	}
}
