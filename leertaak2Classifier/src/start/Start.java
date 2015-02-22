package start;

import classifier.DecisionTree;
import classifier.TrainingSetReader;
import view.Frame;

public class Start {

	public static void main(String[] args) {
		TrainingSetReader reader = new TrainingSetReader();
		DecisionTree tree = reader.getDecisionTree();
		
		Frame frame = new Frame(tree);
		frame.setVisible(true);
		frame.pack();
	}

}
