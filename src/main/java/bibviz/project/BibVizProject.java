package bibviz.project;

import java.io.File;

/**
 * Represents a project.
 * 
 * @author Ankit Gupta
 *
 */
public class BibVizProject {

	private final File projectDir;

	private static final String LUCENE_DB = "publications_lucene";
	private static final String N4J_DB = "publications_neo4j";

	private BibVizProject(File projectDir) {
		this.projectDir = projectDir;
	}

	/**
	 * Return the project directory.
	 * 
	 * @return
	 */
	public File getProjectDir() {
		return projectDir;
	}

	/**
	 * This function opens an existing project.
	 * 
	 * @param projectDir
	 * @return
	 */
	public static BibVizProject openProject(File projectDir) {
		if (isProject(projectDir)) {
			return new BibVizProject(projectDir);
		} else {
			throw new RuntimeException(
					"The selected directory is not a valid project");
		}
	}

	/**
	 * This function creates a project at the given location
	 * 
	 * @param projectDir
	 * @return
	 */
	public static BibVizProject createProject(File projectDir) {

		// if projectDir directory exists and its empty.
		if (projectDir.isDirectory()) {
			if (projectDir.listFiles().length == 0) {
				return new BibVizProject(projectDir);
			} else {
				throw new RuntimeException("Project directory is not empty");
			}

		}

		// if projectDir exists, its a file because the previous condition
		// already checked it for directory
		if (projectDir.exists()) {
			throw new RuntimeException(
					"A file with that name already exists. Choose another directory for the project.");
		} else {
			boolean mkdir = projectDir.mkdir();

			if (mkdir) {
				// create a new project at the location
				return new BibVizProject(projectDir);
			} else {
				throw new RuntimeException(
						"The project directory could not be created. Please check file permissions");
			}
		}
	}

	/**
	 * Checks whether the directory is a BibViz Project
	 * 
	 * @param projectDir
	 *            the directory for the project.
	 * @return true if the projectDir argument is a project, false otherwise.
	 */
	public static boolean isProject(File projectDir) {
		File luceneDir = new File(projectDir, LUCENE_DB);
		File n4jDir = new File(projectDir, N4J_DB);
		if (projectDir.isDirectory() && luceneDir.isDirectory()
				&& n4jDir.isDirectory()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the directory to be used for storing neo4j database
	 * 
	 * @return
	 */
	public File getN4JDir() {
		return new File(projectDir, N4J_DB);
	}

	/**
	 * Returns the directory to be used for storing lucene index
	 * 
	 * @return
	 */
	public File getLuceneDir() {
		return new File(projectDir, LUCENE_DB);
	}

}
