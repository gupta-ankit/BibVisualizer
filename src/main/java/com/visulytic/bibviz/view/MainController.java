package com.visulytic.bibviz.view;

import com.visulytic.bibviz.model.BibVizModel;
import com.visulytic.bibviz.model.LoadDBListener;
import com.visulytic.bibviz.project.BibVizProject;

public class MainController implements LoadDBListener {

	private MainWindow mainWindow;

	public MainController(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	private BibVizProject currentProject;
	private BibVizModel currentModel;

	public void closeCurrentProject() {
		if (currentProject != null) {
			currentModel.close();
			currentProject = null;
			currentModel = null;
		}
	}

	public void openProject(BibVizProject project) {
		currentProject = project;
		currentModel = new BibVizModel(project);
		currentModel.addLoadDBListener(this);
		mainWindow.dbLoaded();
	}

	public BibVizModel getCurrentModel() {
		return currentModel;
	}

	public BibVizProject getCurrentProject() {
		return currentProject;
	}

	@Override
	public void dbLoaded(LoadDBEvent event) {
		mainWindow.dbLoaded();
	}

}
