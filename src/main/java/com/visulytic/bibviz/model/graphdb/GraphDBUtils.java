package com.visulytic.bibviz.model.graphdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import edu.uci.ics.jung.graph.Graph;

public class GraphDBUtils {

	/**
	 * Loads a serialized graph object.
	 * 
	 * @param graphFile
	 *            file containing the serialized graph object.
	 * @return
	 */
	public static Graph<Node, Edge> readGraphObjectFromFile(File graphFile) {
		Graph<Node, Edge> graph = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(graphFile);
			ois = new ObjectInputStream(fis);
			graph = (Graph<Node, Edge>) ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ois.close();
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return graph;
	}

	/**
	 * Writes the graph to the file
	 * @param graphFile the to which the graph object needs to be written to
	 * @param o the object to write.
	 */
	public static void writeGraphToFile(File graphFile, Graph<Node, Edge> o) {
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(graphFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
