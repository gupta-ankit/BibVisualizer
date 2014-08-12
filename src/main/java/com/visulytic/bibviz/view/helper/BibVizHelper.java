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
			nodeObj.addProperty("title", n.getProperty("title").toString());
			nodeObj.addProperty("n4jId", n.getId());
			nodeObj.addProperty("type", "publication");
			nodesArray.add(nodeObj);
		}
		
		JsonArray linksArray = new JsonArray();
		jsonObject.add("links", linksArray);

		model.endTransaction();

		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}
}
