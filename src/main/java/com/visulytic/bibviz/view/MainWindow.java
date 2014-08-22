package com.visulytic.bibviz.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.visulytic.bibviz.Application;
import com.visulytic.bibviz.model.BibVizModel;
import com.visulytic.bibviz.model.graphdb.Edge;
import com.visulytic.bibviz.model.graphdb.Node;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class MainWindow extends JFrame {

	private final FieldSelectionDialog fieldSelectionDialog = new FieldSelectionDialog();

	private AbstractAction selectFieldsAction = new AbstractAction(
			"Select Fields") {

		public void actionPerformed(ActionEvent arg0) {
			if (fieldSelectionDialog.isVisible()) {
				fieldSelectionDialog.setVisible(false);
			} else {
				fieldSelectionDialog.setVisible(true);
				fieldSelectionDialog.setLocation((int) selectFieldsBtn
						.getLocationOnScreen().getX(),
						(int) selectFieldsBtn.getLocationOnScreen().getY()
								+ selectFieldsBtn.getHeight());
			}
		}
	};

	final JButton selectFieldsBtn = new JButton(selectFieldsAction);
	private JTextField searchField;
	private Layout<Node, Edge> layout = new FRLayout<>(
			new DirectedSparseGraph<>());
	private BibGraphViewer vv = new BibGraphViewer(layout);

	public MainWindow() {
		super("Bibtex Visualizer");

		this.setJMenuBar(new MainMenuBar());
		this.setLayout(new MigLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(800, 800));

		searchField = new JTextField(30);

		this.add(searchField, "pushx, growx");
		searchField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Set<Long> search = search();
				vv.setShowOnly(search);
				if (searchField.getText().trim().equals("")) {
					vv.setShowAll(true);
				} else {
					vv.setShowAll(false);
				}
				vv.repaint();
			}
		});

		this.add(selectFieldsBtn, "wrap");
		this.add(vv, "span2, pushx, growx, pushy, growy");
	}

	private Set<Long> search() {
		BibVizModel model = Application.getInstance().getMainController()
				.getCurrentModel();
		if (model == null) {
			JOptionPane.showMessageDialog(this,
					"No project open. Please create/open a project to search");
			return new HashSet<>();
		} else {
			Set<Long> nodeIds = model.search(searchField.getText(),
					fieldSelectionDialog.getSelectedFields());
			return nodeIds;
		}
	}

	public void dbLoaded() {
		BibVizModel model = Application.getInstance().getMainController()
				.getCurrentModel();
		Graph<Node, Edge> graph = model.getGraph();
		layout.setGraph(graph);
		vv.repaint();
	}
}
