package start;

import classifier.DecisionTree;
import classifier.TrainingSetReader;
import view.Frame;

public class Start {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrainingSetReader test = new TrainingSetReader();
		
		DecisionTree tree = test.getDecisionTree();
		
		Frame frame = new Frame(tree);
		frame.setVisible(true);
		frame.pack();
	}

}
