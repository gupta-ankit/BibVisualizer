package com.visulytic.bibviz.view;

import java.awt.Dimension;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import netscape.javascript.JSObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;

import com.google.gson.JsonObject;
import com.visulytic.bibviz.Application;
import com.visulytic.bibviz.model.BibVizModel;
import com.visulytic.bibviz.project.BibVizProject;
import com.visulytic.bibviz.view.helper.BibVizHelper;

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
	private WebView browser;
	private WebEngine engine;

	public MainWindow() {
		super("Bibtex Visualizer");

		this.setJMenuBar(new MainMenuBar());
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
		engine = browser.getEngine();
		engine.load(MainWindow.class.getResource("bibviz.html")
				.toExternalForm());

		JSObject window = (JSObject) engine.executeScript("window");
		window.setMember("bridge", new Bridge());

		engine.documentProperty().addListener(new ChangeListener<Document>() {

			public void changed(ObservableValue<? extends Document> arg0,
					Document arg1, Document arg2) {
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
		BibVizModel model = Application.getInstance().getMainController()
				.getCurrentModel();
		if (model == null) {
			JOptionPane.showMessageDialog(this,
					"No project open. Please create/open a project to search");
			return new HashSet<>();
		} else {
			Set<Integer> nodeIds = model.search(searchField.getText(),
					fieldSelectionDialog.getSelectedFields());

			return nodeIds;
		}
	}

	public void dbLoaded() {
		BibVizModel model = Application.getInstance().getMainController()
				.getCurrentModel();
		String jsonObject = BibVizHelper.getPublicationGraph(model);
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				engine.executeScript("drawGraph(" + jsonObject + ")");
			}
		});
	}

	private class Bridge {

	}
}
