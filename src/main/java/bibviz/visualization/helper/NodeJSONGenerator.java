package bibviz.visualization.helper;

import java.util.List;

import org.neo4j.graphdb.Node;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NodeJSONGenerator {

	public static String getJSON(List<Node> nodes) {
		StringBuilder builder = new StringBuilder();

		JsonArray jsonArray = new JsonArray();

		for (Node n : nodes) {
			jsonArray.add(getJsonElement(n));
		}
		
		return jsonArray.toString();

	}

	private static JsonElement getJsonElement(Node node) {
		JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("title", (String) node.getProperty("title"));
		jsonObject.addProperty("n4jID", node.getId());
		return jsonObject;
	}

}
