package com.visulytic.bibviz.view;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import com.visulytic.bibviz.model.graphdb.AuthorNode;
import com.visulytic.bibviz.model.graphdb.Edge;
import com.visulytic.bibviz.model.graphdb.Node;
import com.visulytic.bibviz.model.graphdb.PublicationNode;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class BibGraphViewer extends VisualizationViewer<Node, Edge> {

	private Set<Long> showOnlyNodes = new HashSet<>();
	boolean showAll = true;
	boolean showAllLabels = false;

	public BibGraphViewer(Layout<Node, Edge> layout) {
		super(layout);
		getRenderContext().setVertexShapeTransformer(vertexShape);
		getRenderContext()
				.setEdgeDrawPaintTransformer(edgeDrawPaintTransformer);
		getRenderContext().setVertexLabelTransformer(
				new Transformer<Node, String>() {

					@Override
					public String transform(Node input) {
						if (showAllLabels
								|| showOnlyNodes.contains(input.getId())) {
							int endIndex = input.getLabel().length() > 33 ? 30
									: input.getLabel().length();
							return input.getLabel().substring(0, endIndex)
									+ "...";
						} else {
							return "";
						}
					}
				});
	}

	public void setShowOnly(Set<Long> idsToKeep) {
		this.showOnlyNodes = idsToKeep;
		Set<Long> neighborsToKeep = new HashSet<>();
		Graph<Node, Edge> graph = BibGraphViewer.this.getGraphLayout()
				.getGraph();
		Collection<Node> vertices = graph.getVertices();
		for (Node n : vertices) {
			if (showOnlyNodes.contains(n.getId())) {
				Collection<Node> neighbors = graph.getNeighbors(n);
				for (Node neighbor : neighbors) {
					neighborsToKeep.add(neighbor.getId());
				}
			}
		}

		showOnlyNodes.addAll(neighborsToKeep);
		showAll = false;
	}

	private Transformer<Edge, Paint> edgeDrawPaintTransformer = new Transformer<Edge, Paint>() {

		@Override
		public Paint transform(Edge input) {
			Graph<Node, Edge> graph = BibGraphViewer.this.getGraphLayout()
					.getGraph();
			Pair<Node> endpoints = graph.getEndpoints(input);
			long firstId = endpoints.getFirst().getId(), secondId = endpoints
					.getSecond().getId();
			if (showOnlyNodes.contains(firstId)
					|| showOnlyNodes.contains(secondId) || showAll) {
				return Color.black;
			} else {
				return BibGraphViewer.this.getBackground();
			}
		}
	};

	private Transformer<Node, Shape> vertexShape = new Transformer<Node, Shape>() {

		@Override
		public Shape transform(Node input) {
			if (showOnlyNodes.contains(input.getId()) || showAll) {
				if (input instanceof PublicationNode) {
					Rectangle2D rect = new Rectangle2D.Double(-15, -15, 30, 30);
					return rect;
				} else if (input instanceof AuthorNode) {
					Ellipse2D circle = new Ellipse2D.Double(-15, -15, 30, 30);
					return circle;
				}
			}
			return new Ellipse2D.Double(0, 0, 0, 0);
		}
	};

	public void setShowAll(boolean b) {
		showAll = b;
	}

}
