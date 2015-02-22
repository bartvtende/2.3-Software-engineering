package start;

import javax.swing.JFrame;

import classifier.DecisionTree;
import classifier.Node;
import classifier.TrainingSetReader;
import view.TreePopup;

public class Start17 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TrainingSetReader trainingSet = new TrainingSetReader();

		DecisionTree tree = trainingSet.getDecisionTree();
		
		TreePopup pop = new TreePopup(tree);
		pop.pack();
	}
	
	

}
