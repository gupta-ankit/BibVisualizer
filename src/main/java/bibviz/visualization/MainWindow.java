package bibviz.visualization;

import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Set;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import com.google.gson.JsonObject;

import bibviz.model.BibVizModel;
import bibviz.visualization.helper.BibVizHelper;

public class MainWindow extends JFrame {

	private final FieldSelectionDialog fieldSelectionDialog = new FieldSelectionDialog();
	private final JFXPanel fxPanel;

	private AbstractAction selectFieldsAction = new AbstractAction(
			"Select Fields") {

		public void actionPerformed(ActionEvent arg0) {
			if (fieldSelectionDialog.isVisible()) {
				fieldSelectionDialog.setVisible(false);
			} else {
				fieldSelectionDialog.setVisible(true);
				fieldSelectionDialog.setLocation((int) selectFieldsBtn
						.getLocationOnScreen().getX(),
						(int) selectFieldsBtn.getLocationOnScreen().getY()
								+ selectFieldsBtn.getHeight());
			}
		}
	};

	final JButton selectFieldsBtn = new JButton(selectFieldsAction);
	private JTextField searchField;
	private BibVizModel model;
	private WebView browser;

	public MainWindow(BibVizModel model) {
		super("Bibtex Visualizer");

		this.model = model;
		this.setLayout(new MigLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 800));

		searchField = new JTextField(30);
		searchField.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Set<Integer> nodeIds = search();
				System.err.println("Show Only: " + nodeIds);
				final String showOnlyNodes = nodeIds.toString();
				Platform.runLater(new Runnable() {

					public void run() {
						browser.getEngine().executeScript(
								"showOnlyNodes(" + showOnlyNodes + ")");
					}
				});
			}
		});

		fxPanel = new JFXPanel();

		this.add(searchField, "pushx, growx");
		this.add(selectFieldsBtn, "wrap");
		this.add(fxPanel, "span2, pushx, growx, pushy, growy");
	}

	public void initFX() {
		browser = new WebView();
		final WebEngine engine = browser.getEngine();
		engine.load(MainWindow.class.getResource("bibviz.html")
				.toExternalForm());
//		try {
//			String d3tip = IOUtils.toString(MainWindow.class.getResourceAsStream("d3-tip.js"));
//			engine.executeScript(d3tip);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		engine.documentProperty().addListener(new ChangeListener<Document>() {

			public void changed(ObservableValue<? extends Document> arg0,
					Document arg1, Document arg2) {
				String sample_json_object = BibVizHelper
						.getPublicationGraph(model);
				engine.executeScript("drawGraph(" + sample_json_object + ")");
				enableFirebug(engine);
			}

			private void enableFirebug(WebEngine engine) {
				engine.executeScript("if (!document.getElementById('FirebugLite')){E = document['createElement' + 'NS'] && document.documentElement.namespaceURI;E = E ? document['createElement' + 'NS'](E, 'script') : document['createElement']('script');E['setAttribute']('id', 'FirebugLite');E['setAttribute']('src', 'https://getfirebug.com/' + 'firebug-lite.js' + '#startOpened');E['setAttribute']('FirebugLite', '4');(document['getElementsByTagName']('head')[0] || document['getElementsByTagName']('body')[0]).appendChild(E);E = new Image;E['setAttribute']('src', 'https://getfirebug.com/' + '#startOpened');}");
			}
		});
		Scene scene = new Scene(browser);
		fxPanel.setScene(scene);
	}

	private Set<Integer> search() {
		Set<Integer> nodeIds = model.search(searchField.getText(),
				fieldSelectionDialog.getSelectedFields());

		return nodeIds;
	}
}
