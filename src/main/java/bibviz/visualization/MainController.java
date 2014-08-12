package bibviz.visualization;

import bibviz.model.BibVizModel;
import bibviz.model.LoadDBListener;
import bibviz.project.BibVizProject;

public class MainController implements LoadDBListener {

	private MainWindow mainWindow;

	public MainController(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	private BibVizProject currentProject;
	private BibVizModel currentModel;

	public void closeCurrentProject() {
		if (currentProject != null) {
			currentProject = null;
			currentModel = null;
			currentModel.close();
		}
	}

	public void openProject(BibVizProject project) {
		currentProject = project;
		currentModel = new BibVizModel(project);
		currentModel.addLoadDBListener(this);
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
