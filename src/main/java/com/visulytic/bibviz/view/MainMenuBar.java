package com.visulytic.bibviz.view;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.Key;

import com.visulytic.bibviz.Application;
import com.visulytic.bibviz.BibtexDBLoader;
import com.visulytic.bibviz.model.BibVizModel;
import com.visulytic.bibviz.project.BibVizProject;

public class MainMenuBar extends JMenuBar {

	public MainMenuBar() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.add(new JMenuItem(newProjectAction));
		fileMenu.add(new JMenuItem(openProjectAction));

		JMenu projectMenu = new JMenu("Project");
		projectMenu.add(new JMenuItem(loadBibAction));

		this.add(fileMenu);
		this.add(projectMenu);
	}

	private final AbstractAction loadBibAction = new AbstractAction(
			"Load Bibliography") {

		@Override
		public void actionPerformed(ActionEvent event) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public boolean accept(File f) {
					return f.isDirectory() || f.getName().endsWith(".bib");
				}

				@Override
				public String getDescription() {
					return "*.bib files";
				}

			});

			int retVal = fileChooser.showOpenDialog(null);
			if (retVal == JFileChooser.APPROVE_OPTION) {
				BibTeXDatabase db = BibtexDBLoader.ingest(fileChooser
						.getSelectedFile());
				BibVizModel model = Application.getInstance()
						.getMainController().getCurrentModel();
				model.loadDatabase(db);
			}
		}
	};

	private final AbstractAction newProjectAction = new AbstractAction(
			"New Project") {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				BibVizProject project = BibVizProject.createProject(fileChooser
						.getSelectedFile());
				MainController mainController = Application.getInstance()
						.getMainController();
				mainController.closeCurrentProject();
				mainController.openProject(project);
			}
		}
	};

	private final AbstractAction openProjectAction = new AbstractAction(
			"Open Project") {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			fileChooser.setFileFilter(new FileFilter() {

				@Override
				public String getDescription() {
					return "BibVisualizer Project";
				}

				@Override
				public boolean accept(File f) {
					if (BibVizProject.isProject(f) || containsDirectory(f)) {
						return true;
					}
					return false;
				}
			});
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				BibVizProject project = BibVizProject.openProject(fileChooser
						.getSelectedFile());
				MainController mainController = Application.getInstance()
						.getMainController();
				mainController.closeCurrentProject();
				mainController.openProject(project);
			}
		}
	};

	private static boolean containsDirectory(File dir) {
		if (dir.isDirectory() && dir.listFiles() != null) {

			for (File f : dir.listFiles()) {
				if (f.isDirectory()) {
					return true;
				}
			}
		}

		return false;
	}
}
