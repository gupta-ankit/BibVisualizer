package bibviz;

import java.io.File;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;

import javafx.application.Platform;
import bibviz.model.BibVizModel;
import bibviz.visualization.MainWindow;

public class Application {

	public static void main(String[] args) {
		final BibVizModel model = loadModel();
		final MainWindow mainWindow = new MainWindow(model);
		Platform.runLater(new Runnable() {

			public void run() {

				mainWindow.initFX();

				mainWindow.pack();
				mainWindow.setVisible(true);
				mainWindow.setLocationRelativeTo(null);

			}
		});
	}

	public static BibVizModel loadModel() {
		BibTeXDatabase db = BibtexDBLoader.ingest(new File("sample.bib"));
		BibVizModel model = new BibVizModel();

		int counter = 0;
		for (Key k : db.getEntries().keySet()) {
			BibTeXEntry e = db.getEntries().get(k);
			model.addEntry(k, e);
			counter++;
			if (counter > 3) {
				break;
			}
		}

		return model;
	}
}
