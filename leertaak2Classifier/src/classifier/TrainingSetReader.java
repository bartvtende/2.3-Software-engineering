package classifier;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads the trainingsSet text file and builds a DecisionTree of it
 * 
 * @author Bart van 't Ende and Jan-Bert van Slochteren
 * @version 1.0
 *
 */
public class TrainingSetReader {	
	// Paths to the text files
	private final String TRAININGSET = "./src/test/TrainingSet.txt";
	private final String OPTIONS = "./src/test/OptiesText.txt";

	// BufferedReaders
    private BufferedReader trainingSetReader;
    private BufferedReader optionsReader;
    
    // Maps needed to build a DecisionTree
    private Map<Item, String> trainingsSet = new HashMap<Item, String>();
    private Map<String, FeatureType> features = new HashMap<String, FeatureType>();
    private String[] options = new String[8];
    
    // Create a FeatureType with Yes or No
    private final FeatureType featureType = new FeatureType("YesNo", new String[] { "1", "0" });
    
    // The DecisionTree object
    private DecisionTree tree;
    
	/**
	 * Constructor
	 */
	public TrainingSetReader() {    	
		try {
			// Create new BufferedReader
			this.trainingSetReader = new BufferedReader(new FileReader(TRAININGSET));
			this.optionsReader = new BufferedReader(new FileReader(OPTIONS));

			// Parse the options (features)
			readOptions();
			
			// Parse the training set
			this.tree = readTrainingSet();
		} catch (FileNotFoundException e) {
			System.out.println("FileNotFoundException: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}

	/**
	 * Getter for the DecisionTree object
	 * 
	 * @return
	 */
	public DecisionTree getDecisionTree() {
		return tree;
	}

	/**
	 * Reads the trainingsSet text file and builds a map with all the items from it
	 * 
	 * @return
	 * @throws IOException
	 */
	private DecisionTree readTrainingSet() throws IOException {
		// Skip the first two lines for now
		trainingSetReader.readLine();
		trainingSetReader.readLine();
		
		String line;
		// Loop over the items
		while ((line = trainingSetReader.readLine()) != null) {
			String[] itemProperties = line.split(";");
			String itemName = itemProperties[9];
			
			// Fill the trainingsSet with this rows properties
			this.trainingsSet.put(getItem(itemProperties), itemName);
		}
		
		// Build a new DecisionTree from the trainingsSet and features
		DecisionTree newTree = new DecisionTree(trainingsSet, features);
		
		return newTree;
	}
	
	/**
	 * Reads the options text file and builds a map with all the features from it
	 * 
	 * @throws IOException
	 */
	private void readOptions() throws IOException {		
		String line;
		int index = 0;
		
		// Parse the options text file and make an array out of it
		while ((line = optionsReader.readLine()) != null) {
			this.options[index] = line;
			index++;
		}
		
		// Fill the features map with all the available features
		for(String featureName : options) {
			this.features.put(featureName, featureType);
		}	
	}
	
	/**
	 * Creates a new Item object from the given array
	 * 
	 * @param itemProperties
	 * @return
	 */
	private Item getItem(String[] itemProperties) {
		Feature[] features = new Feature[8];
		
		for(int i = 0; i < 8; i++) {
			features[i] = new Feature(this.options[i], itemProperties[i+1], featureType);
		}
		
		Item newItem = new Item(itemProperties[0], features);
		
		return newItem;
	}
	
}
