package com.visulytic.bibviz.view.helper;

import java.util.List;

import org.neo4j.graphdb.Node;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.visulytic.bibviz.model.BibVizModel;

public class BibVizHelper {

	public static String getPublicationGraph(BibVizModel model) {

		model.beginTransaction();
		List<Node> allNodes = model.getAllNodes();
		JsonObject jsonObject = new JsonObject();

		JsonArray nodesArray = new JsonArray();
		jsonObject.add("nodes", nodesArray);

		for (Node n : allNodes) {
			JsonObject nodeObj = new JsonObject();
			nodeObj.addProperty("n4jId", n.getId());
			System.err.println(n.getLabels());
			String label = n.getLabels().iterator().next().toString();
			if (label.equals("Publication")) {
				nodeObj.addProperty("type", "publication");
				nodeObj.addProperty("label", n.getProperty("title").toString());
			} else if (label.equals("Author")) {
				nodeObj.addProperty("label", n.getProperty("name").toString());
				nodeObj.addProperty("type", "author");
			} else {
				System.err.println("No Label match");
			}

			nodesArray.add(nodeObj);
		}

		JsonArray linksArray = new JsonArray();
		jsonObject.add("links", linksArray);

		model.endTransaction();

		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
}
