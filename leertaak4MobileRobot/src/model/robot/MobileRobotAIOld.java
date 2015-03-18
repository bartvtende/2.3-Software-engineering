package model.robot;

import model.virtualmap.OccupancyMap;

import java.io.PipedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PipedOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Title : The Mobile Robot Explorer Simulation Environment v2.0 Copyright: GNU
 * General Public License as published by the Free Software Foundation Company :
 * Hanze University of Applied Sciences
 *
 * @author Bart van 't Ende (2015)
 * @author Jan-Bert van Slochteren (2015)
 * @author Davide Brugali (2002)
 * @version 2.0
 */

public class MobileRobotAIOld implements Runnable {

	private static final int WALL_DISTANCE = 2;

	private static final int LASER_RANGE = 10;

	private static final int TOP = 270;
	private static final int RIGHT = 360;
	private static final int BOTTOM = 90;
	private static final int LEFT = 180;

	private final OccupancyMap map;
	private final MobileRobot robot;

	private PipedInputStream pipeIn;
	private BufferedReader input;
	private PrintWriter output;

	private double position[];
	private double measures[];

	private String result = "";

	private boolean running;

	public MobileRobotAIOld(MobileRobot robot, OccupancyMap map) {
		this.map = map;
		this.robot = robot;
	}

	/**
	 * First attempt on AI
	 */
	public void run() {
		this.running = true;
		this.position = new double[3];
		this.measures = new double[360];

		System.out.println("Robot is now booting..");
		while (running) {
			try {
				this.result = "";

				this.pipeIn = new PipedInputStream();
				this.input = new BufferedReader(new InputStreamReader(pipeIn));
				this.output = new PrintWriter(new PipedOutputStream(pipeIn),
						true);

				robot.setOutput(output);

				// Step 1: Scan the surroundings with the laser and save the
				// results
				scanLaser();

				// Step 2: Check the map and determine if the robot is following
				// the wall or not
				boolean foundWall = findMovement();
				boolean wallAhead = wallAhead();

				if (wallAhead) {
					System.out.println("Wall ahead");
					moveLeft();
				}
				
				int steps = getSteps();
				if (steps == 0) {
					moveLeft();
					moveForward(getSteps());
				} else {
					// Step 3: Determine the move
					if (foundWall) {
						moveForward(getSteps());
					} else if (!wallAhead) {
						// Try to find a guiding wall
						moveRight(); // Rotate right (to find the wall)
						moveForward(getSteps()); // Calculate the amount of steps
													// and go forward
					}
				}

				// Step 4: Check if the exploration is completed
				isCompleted();
			} catch (IOException ioe) {
				System.err.println("Execution stopped");
				running = false;
			}
		}
		System.out.println("Robot is now shutting down..");
	}

	/**
	 * Returns true if the robot is currently following the wall
	 * 
	 * @return
	 */
	private boolean findMovement() {
		boolean foundWall = false;

		int[] positionData = getPosition();

		int x = positionData[0];
		int y = positionData[1];

		// Get the position to the right
		int directionToRight = getDirectionToRight();

		// Get the coordinates of the supposed wall
		int[] wallCoordinates = getWallCoordinates(x, y, directionToRight);

		int xRight = wallCoordinates[0];
		int yRight = wallCoordinates[1];

		// Determine whether or not the robot is following the wall
		if (map.getGrid()[xRight][yRight] == map.getObstacle()) {
			// A wall has been found
			foundWall = true;
			System.out.println("Found a wall to follow, yey");
		}

		// Return the boolean value
		return foundWall;
	}

	/**
	 * Returns the amount of steps that the robot can move
	 * 
	 * @param forwardSpace
	 *            the free space in front of th robot.
	 * @return
	 */
	private int getSteps() {
		int amountOfSteps;
		
		// Get the robot's current position and direction
		int[] positionData = getPosition();

		int x = positionData[0];
		int y = positionData[1];
		int direction = positionData[2];

		// Get the position to the right
		int directionToRight = getDirectionToRight();

		// Get the coordinates of the supposed wall
		int[] wallCoordinates = getWallCoordinates(x, y, directionToRight);

		int xRight = wallCoordinates[0];
		int yRight = wallCoordinates[1];

		amountOfSteps = LASER_RANGE;

		int[] coordinates = new int[4];

		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = xRight;
		coordinates[3] = yRight;
		
		boolean foundWall = false;

		/*
		while (i < LASER_RANGE && !obstacleIsFound) {
			char distance = getDistance(coordinates, direction, i);
			
			if (distance == map.getObstacle()) {
				amountOfSteps = i - WALL_DISTANCE;
				obstacleIsFound = true;
			}
		}*/
		/*
		while (i < LASER_RANGE && !obstacleIsFound) {
			char[] distances = new char[2];

			try {
				distances = getDistances(coordinates, direction, i);
			} catch (Exception e) {
				System.out.println("Array out of bounds, but continuing.");
			}
			
			if (distances[1] != map.getObstacle()) {
				if (foundWall) {
					if (distances[1] == map.getEmpty()) {
						amountOfSteps = (i + 1) + WALL_DISTANCE;
						obstacleIsFound = true;
					} else if (distances[1] == map.getUnknown()) {
						amountOfSteps = (i + 1);
						obstacleIsFound = true;
					}
				}
			} else if (distances[1] == map.getObstacle()) {
				foundWall = true;
			}
			if (distances[0] == map.getObstacle()
					|| distances[0] == map.getUnknown()) {
				amountOfSteps = (i + 1) - WALL_DISTANCE;
				obstacleIsFound = true;
			}
			
			System.out.println("----");
			System.out.println("position x: " + x + " position y: " + y
					+ " direction: " + direction);
			System.out.println("Number: " + i);
			System.out.println(distances[0]);
			System.out.println(distances[1]);
			
			if (amountOfSteps < 0) {
				amountOfSteps = 0;
			}
			
			i++;
		}*/

		
		// Check the environment in front of the robot
		for (int i = 1; i <= LASER_RANGE; i++) {
			int newAmountOfSteps;
			char[] distances = new char[2];

			try {
				distances = getDistances(coordinates, direction, i);
			} catch (Exception e) {
				System.out.println("Array out of bounds, but continuing.");
			}

			if (distances[0] == map.getObstacle()
					|| distances[0] == map.getUnknown()) {
				newAmountOfSteps = i - WALL_DISTANCE;
				if (amountOfSteps > newAmountOfSteps && newAmountOfSteps > 0) {
					amountOfSteps = newAmountOfSteps;
				} else if (newAmountOfSteps <= 0) {
					amountOfSteps = i - 1;
				}
			}
			if (distances[1] != map.getObstacle()) {
				if (foundWall) {
					if (distances[1] == map.getUnknown()) {
						newAmountOfSteps = i - WALL_DISTANCE;
						System.out.println(i + " unknown");
						if (amountOfSteps > newAmountOfSteps) {
							amountOfSteps = newAmountOfSteps;
						}
					}
					if (distances[1] == map.getEmpty()) {
						newAmountOfSteps = i + WALL_DISTANCE;
						System.out.println(i + " empty");
						if (amountOfSteps > newAmountOfSteps) {
							amountOfSteps = newAmountOfSteps;
						}
					}
				}
			} else if (distances[1] == map.getObstacle()) {
				foundWall = true;
			}
			
			System.out.println("----");
			System.out.println("Position x: " + x + ", position y: " + y
					+ ", direction: " + direction);
			System.out.println("Number: " + i);
			System.out.println(distances[0]);
			System.out.println(distances[1]);
		}

		System.out.println("--------------------------------");
		System.out.println(amountOfSteps);

		if (amountOfSteps < 0) {
			amountOfSteps = 0;
		}

		return amountOfSteps;
	}

	private boolean wallAhead() {
		// Get the robot's current position and direction
		int[] positionData = getPosition();

		int x = positionData[0];
		int y = positionData[1];
		int direction = positionData[2];

		// Get the coordinates of the supposed wall
		int[] wallCoordinates = getWallCoordinates(x, y, direction);

		int xRight = wallCoordinates[0];
		int yRight = wallCoordinates[1];

		if (map.getGrid()[xRight][yRight] == map.getObstacle()) {
			return true;
		}
		
		return false;
	}

	/**
	 * Checks if all the walls are explored and stop the robot
	 */
	private void isCompleted() {
		// TODO
	}

	private void parsePosition(String value, double position[]) {
		int indexInit;
		int indexEnd;
		String parameter;

		indexInit = value.indexOf("X=");
		parameter = value.substring(indexInit + 2);
		indexEnd = parameter.indexOf(' ');
		position[0] = Double.parseDouble(parameter.substring(0, indexEnd));

		indexInit = value.indexOf("Y=");
		parameter = value.substring(indexInit + 2);
		indexEnd = parameter.indexOf(' ');
		position[1] = Double.parseDouble(parameter.substring(0, indexEnd));

		indexInit = value.indexOf("DIR=");
		parameter = value.substring(indexInit + 4);
		position[2] = Double.parseDouble(parameter);
	}

	private void parseMeasures(String value, double measures[]) {
		for (int i = 0; i < 360; i++) {
			measures[i] = 100.0;
		}
		if (value.length() >= 5) {
			value = value.substring(5); // removes the "SCAN " keyword

			StringTokenizer tokenizer = new StringTokenizer(value, " ");

			double distance;
			int direction;
			while (tokenizer.hasMoreTokens()) {
				distance = Double.parseDouble(tokenizer.nextToken()
						.substring(2));
				direction = (int) Math.round(Math.toDegrees(Double
						.parseDouble(tokenizer.nextToken().substring(2))));
				if (direction == 360) {
					direction = 0;
				}
				measures[direction] = distance;
				// Printing out all the degrees and what it encountered.
				// System.out.println("Direction = " + direction +
				// " - Distance = " + distance);
			}
		}
	}

	/**
	 * Gets the position of the robot and scans the environment.
	 * 
	 * @throws IOException
	 */
	private void scanLaser() throws IOException {
		robot.sendCommand("R1.GETPOS");
		this.result = input.readLine();
		parsePosition(result, position);

		robot.sendCommand("L1.SCAN");
		result = input.readLine();
		parseMeasures(result, measures);
		map.drawLaserScan(position, measures);

		// Transform 0 into 360
		if (Math.round(position[2]) == 0) {
			position[2] = RIGHT;
		}
	}

	/**
	 * Rotates the robot 90 degrees to the right
	 * 
	 * @throws IOException
	 */
	private void moveRight() throws IOException {
		robot.sendCommand("P1.ROTATERIGHT 90");
		result = input.readLine();

		rescanPosition();
	}

	/**
	 * Rotates the robot 90 degrees to the left
	 * 
	 * @throws IOException
	 */
	private void moveLeft() throws IOException {
		robot.sendCommand("P1.ROTATELEFT 90");
		result = input.readLine();
	}

	/**
	 * Moves the robot forward for a x amount of steps
	 * 
	 * @param steps
	 * @throws IOException
	 */
	private void moveForward(int steps) throws IOException {
		steps *= 10;
		this.robot.sendCommand("P1.MOVEFW " + steps);
		result = input.readLine();
	}

	/**
	 * Returns the rounded position, containing: x, y and angle information
	 * 
	 * @return positions
	 */
	private int[] getPosition() {
		int[] positions = new int[3];

		int x = ((int) Math.round(position[0]) / 10);
		int y = ((int) Math.round(position[1]) / 10);
		int direction = (int) Math.round(position[2]);

		positions[0] = x;
		positions[1] = y;
		positions[2] = direction;

		return positions;
	}

	/**
	 * Returns the direction angle when the robot rotates right
	 * 
	 * @param direction
	 * @return
	 */
	private int getDirectionToRight() {
		int direction = (int) Math.round(position[2]);
		int directionToRight = direction;
		if (direction % 360 == 0)
			directionToRight -= 360;
		directionToRight += 90;

		return directionToRight;
	}

	/**
	 * Rescans the current position of the robot
	 * 
	 * @throws IOException
	 */
	private void rescanPosition() throws IOException {
		robot.sendCommand("R1.GETPOS");
		this.result = input.readLine();
		parsePosition(result, position);
	}

	/**
	 * Returns the coordinates from the wall
	 * 
	 * @param x
	 * @param y
	 * @param directionToRight
	 * @return
	 */
	private int[] getWallCoordinates(int x, int y, int directionToRight) {
		int[] wallCoordinates = new int[2];

		switch (directionToRight) {
		case TOP:
			y -= WALL_DISTANCE;
			break;
		case RIGHT:
			x += WALL_DISTANCE;
			break;
		case BOTTOM:
			y += WALL_DISTANCE;
			break;
		case LEFT:
			x -= WALL_DISTANCE;
			break;
		}

		wallCoordinates[0] = x;
		wallCoordinates[1] = y;

		return wallCoordinates;
	}

	private char[] getDistances(int[] coordinates, int direction, int i)
			throws ArrayIndexOutOfBoundsException {
		char[] distances = new char[2];

		switch (direction) {
		case TOP:
			distances[0] = map.getGrid()[coordinates[0]][coordinates[1] - i];
			distances[1] = map.getGrid()[coordinates[2]][coordinates[3] - i];
			break;
		case RIGHT:
			distances[0] = map.getGrid()[coordinates[0] + i][coordinates[1]];
			distances[1] = map.getGrid()[coordinates[2] + i][coordinates[3]];
			break;
		case BOTTOM:
			distances[0] = map.getGrid()[coordinates[0]][coordinates[1] + i];
			distances[1] = map.getGrid()[coordinates[2]][coordinates[3] + i];
			break;
		case LEFT:
			distances[0] = map.getGrid()[coordinates[0] - i][coordinates[1]];
			distances[1] = map.getGrid()[coordinates[2] - i][coordinates[3]];
			break;
		}
		return distances;
	}
	
	//EXPLORED THINGS 
		//TODO
		private boolean isExplored(){
			//Ga over het hele bord als een unknown aan een empty grenst dan return false, aan het einde return true; 
			for(int i = 0; i<map.getGrid().length;i++){
				for( int j = 0; j<map.getGrid()[0].length;j++){
					if(map.getGrid()[i][j] == map.getUnknown()){
						if(bordersCard(i, j, map.getEmpty())){
							return false;
						}
					}
				}
			}
			return true;
			
		}
		private boolean bordersCard(int row, int column, char cardChar){
			// TODO
			if (isValidRow(row - 1)) {
				if (map.getGrid()[row - 1][column] != 0) {
					//System.out.println(map.getGrid()[row - 1][column].getCardChar());
					if (cardChar == map.getGrid()[row - 1][column]) {
						return true;
					}
				}
			}
			if (isValidRow(row + 1)) {
				if (map.getGrid()[row + 1][column] != 0) {
					//System.out.println(map.getGrid()[row + 1][column].getCardChar());
					if (cardChar == map.getGrid()[row + 1][column]) {
						return true;
					}
				}
			}
			if (isValidColumn(column - 1)) {
				if (map.getGrid()[row][column - 1] != 0) {
					//System.out.println(map.getGrid()[row][column-1].getCardChar());
					if (cardChar == map.getGrid()[row][column - 1]) {
						return true;
					}
				}
			}
			if (isValidColumn(column + 1)) {
				if (map.getGrid()[row][column + 1] != 0) {
					//System.out.println(map.getGrid()[row][column+1].getCardChar());
					if (cardChar == map.getGrid()[row][column + 1]) {
						return true;
					}
				}
				
			}
			return false;
		}
		
		//checks if row is on the the board
	    private boolean isValidRow(int row){
	   	 if(row<0){
	   		 return false;
	   	 }
	   	 if(row>=map.getGrid().length){
	   		 return false;
	   	 }
	   	 return true;
	    }

	    //checks if column is on the the board
	    private boolean isValidColumn(int column){
	    	
	    
	   	 if(column<0){
	   		 return false;
	   	 }
	   	 if(map.getGrid().length>0){
	   		 if(column>=map.getGrid()[0].length){
	   			 return false;
	   		 }
	   	 }
	   	 return true;
	    }

}