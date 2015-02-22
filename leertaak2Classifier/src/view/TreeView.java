package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JPanel;

import classifier.DecisionTree;
import classifier.Node;
import classifier.TrainingSetReader;

public class TreeView extends JPanel {

	private DecisionTree tree;
    private static final int Y_OFFSET = 200;
	
	
	public TreeView() {
		TrainingSetReader trainingSet = new TrainingSetReader();

		tree = trainingSet.getDecisionTree();
		
		this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(4500, 4500));
        this.setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("Arial", 0, 10));

		Node root = tree.getRoot();

		drawNode(root, this.getWidth() / 2, TreeView.Y_OFFSET,
				this.getWidth() / 4, 20, g, "1");

	}

	public void drawNode(Node n, int x, int y, int xOffset, int yOffset,
			Graphics g, String direction) {
		g.setColor(Color.YELLOW);
		g.drawString(n.getLabel(), x, y);
		Map<String, Node> children = n.getArcs();

		int xPos = 0;
		int yPos = 0;

		for (Iterator<String> child = children.keySet().iterator(); child
				.hasNext();) {
			String newDirection = child.next();

			Node childNode = children.get(direction);

			if (newDirection.equals("1")) {
				xPos = x - xOffset;
				yPos = y + yOffset;
			} else if (newDirection.equals("0")) {
				xPos = x + xOffset;
				yPos = y + yOffset;
			} else {
				throw new IllegalArgumentException("are you insane?");
			}
			g.setColor(Color.WHITE);
			g.drawLine(x - 1, y - 1, xPos - 1, yPos - 1);

			drawNode(childNode, xPos, yPos, xOffset / 2, yOffset, g,
					newDirection);
		}
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(tree.toString());
		repaint();
	}

}
