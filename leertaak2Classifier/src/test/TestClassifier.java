package test;

import classifier.*;
import junit.framework.TestCase;

public class TestClassifier extends TestCase {

	public TestClassifier(String arg0) {
		super(arg0);
	}

	private DecisionTree buildTree() {
		// AC root node
		Node root = new Node("AC");

		// ABS nodes
		Node n1 = new Node("ABS");
		Node n2 = new Node("ABS");

		// Add ABS nodes to root
		root.addChild("yes", n1);
		root.addChild("no", n2);

		// Radio nodes
		Node radioL1 = new Node("Radio");
		Node radioL2 = new Node("Radio");
		Node radioR1 = new Node("Radio");
		Node radioR2 = new Node("Radio");

		// Add radio to left nodes
		n1.addChild("yes", radioL1);
		n1.addChild("no", radioL2);

		// Add radio to right nodes
		n2.addChild("yes", radioR1);
		n2.addChild("no", radioR2);

		// Leaves
		Node h1 = new Node("high");

		Node m1 = new Node("medium");
		Node m2 = new Node("medium");
		Node m3 = new Node("medium");

		Node l1 = new Node("low");
		Node l2 = new Node("low");
		Node l3 = new Node("low");
		Node l4 = new Node("low");

		radioL1.addChild("yes", h1);
		radioL1.addChild("no", m1);

		radioL2.addChild("yes", m2);
		radioL2.addChild("no", l1);

		radioR1.addChild("yes", m3);
		radioR1.addChild("no", l2);

		radioR2.addChild("yes", l3);
		radioR2.addChild("no", l4);

		return new DecisionTree(root);
	}

	public void testCategory() {

		DecisionTree dt = buildTree();

		FeatureType yn = new FeatureType("YesNo", new String[] { "yes", "no" });

		Feature[] features = new Feature[] { new Feature("AC", "yes", yn),
				new Feature("ABS", "yes", yn), new Feature("Radio", "yes", yn) };

		Item item = new Item("car", features);

		String category = dt.assignCategory(item);
		assertEquals("high", category);

		item.setFeatureValue("AC", "no");
		category = dt.assignCategory(item);
		assertEquals("medium", category);

		item.setFeatureValue("ABS", "no");
		category = dt.assignCategory(item);
		assertEquals("low", category);

		item.setFeatureValue("Radio", "no");
		category = dt.assignCategory(item);
		assertEquals("low", category);
	}
}
