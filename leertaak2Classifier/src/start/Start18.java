package start;

import classifier.DecisionTree;
import classifier.Node;
import view.Frame;

public class Start18 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DecisionTree tree = createDecisionTree(); 
		
		Frame frame = new Frame(tree);
		frame.setVisible(true);
		frame.pack();
	}

	private static DecisionTree createDecisionTree() {
		// TODO Auto-generated method stub
		// AC root node
				Node root = new Node("Brand");
				
				Node n1 = new Node("Model");
				
				root.addChild("Ford", n1);
				
				// ABS nodes
				Node m1 = new Node("Colour");
				

				// Add ABS nodes to root
				n1.addChild("Model T", m1);

				// Radio nodes
				Node blue = new Node("Black");
				Node red = new Node("Black");
				Node green = new Node("Black");
				

				// Add radio to left nodes
				m1.addChild("Blue", blue);
				m1.addChild("Red", red);
				m1.addChild("Green", green);

				return new DecisionTree(root);
	}

}
