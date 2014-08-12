package bibviz;

import javax.swing.SwingUtilities;

import javafx.application.Platform;
import bibviz.visualization.MainController;
import bibviz.visualization.MainWindow;

public class Application {

	// view
	private final MainWindow mainWindow = new MainWindow();
	// controller
	private final MainController mainController = new MainController(
			mainWindow);

	private static final Application INSTANCE = new Application();

	public static void main(String[] args) {
		INSTANCE.init();
	}

	public void init() {
		Platform.runLater(new Runnable() {
			public void run() {
				mainWindow.initFX();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						mainWindow.pack();
						mainWindow.setVisible(true);
						mainWindow.setLocationRelativeTo(null);
					}
				});
			}
		});
	}

	public static Application getInstance() {
		return INSTANCE;
	}

	public MainController getMainController() {
		return mainController;
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}
}
