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
 * @version 1.3
 */

public class MobileRobotAI implements Runnable {

	private static final int WALL_DISTANCE = 2;
	private static final int SCAN_RANGE = 10;

	private static final int TOP = 270;
	private static final int RIGHT = 360;
	private static final int BOTTOM = 90;
	private static final int LEFT = 180;

	private static final String ROTATE_LEFT = "P1.ROTATELEFT 90";
	private static final String ROTATE_RIGHT = "P1.ROTATERIGHT 90";

	private static final int BEGIN = 1;
	private static final int WALL = 2;

	private final OccupancyMap map;
	private final MobileRobot robot;

	private PipedInputStream pipeIn;
	private BufferedReader input;
	private PrintWriter output;

	private double position[];
	private double measures[];

	private String result = "";

	private boolean running;

	public MobileRobotAI(MobileRobot robot, OccupancyMap map) {
		this.map = map;
		this.robot = robot;
	}

	/**
	 * AI implementation for the robot
	 */
	public void run() {
		this.running = true;
		this.position = new double[3];
		this.measures = new double[360];

		boolean begin = true;
		boolean turnAroundWall = false;

		System.out.println("Robot is now booting..");
		while (running) {
			try {
				this.result = "";

				this.pipeIn = new PipedInputStream();
				this.input = new BufferedReader(new InputStreamReader(pipeIn));
				this.output = new PrintWriter(new PipedOutputStream(pipeIn),
						true);

				robot.setOutput(output);

				// Step 1: Scan the surroundings with the laser and sonar and
				// save the results
				scanLaser();
				scanSonar();

				// Step 2: Check if the exploration is just beginning
				while (begin) {
					if (!scanWallForward()) {
						System.out.println("Beginning state");
						// Go forward until we scan a wall forward
						moveForward(getSteps(BEGIN));
						scanLaser();
						scanSonar();
					} else {
						System.out.println("Ending begin state - turn left");
						// Turn left
						rotate(ROTATE_LEFT);
						begin = false;
					}
				}

				// Step 3: Check if the robot follows the right wall and there's
				// a wall in front
				if (scanWallForward() && scanWallRight()) {
					System.out
							.println("Found a wall forward and right = turn left");
					// Go left
					rotate(ROTATE_LEFT);
				} else if (turnAroundWall) {
					// Step 4: Check turnAroundWall, true means that the robot
					// needs to maneuver around the wall
					System.out
							.println("Found an ending wall, manouvering around it");
					// Go forward, right and forward
					turnAroundWall();
					turnAroundWall = false;
				} else if (scanWallRight() && !turnAroundWall) {
					// Step 5: Check if the robot is following the wall
					int stepsForward = getSteps(WALL);
					if (stepsForward == 0) {
						// Wall is ending, so set boolean turnAroundWall to true
						// to trigger the wall maneuver
						System.out
								.println("Found no wall to the right, commanding to move around it");
						turnAroundWall = true;
					} else {
						// Go forward, wall is not ending
						System.out
								.println("Found a wall right, moving forward");
						moveForward(stepsForward);
					}
				} else {
					System.out
							.println("Something went wrong, oops. Couldn't find a direciton");
					System.out.println("Printing position - x: " + position[1]
							+ ", y: " + position[1] + ", dir: " + position[2]);
					System.out.println("Printing scanWallForward: "
							+ scanWallForward() + ", scanWallRight: "
							+ scanWallRight() + ", turnAroundWall: "
							+ turnAroundWall);
					// Start over (begin to true), fail safe
					begin = true;
				}

				// Step 6: Check if the exploration is completed and stop it
				if (isExplored()) {
					running = false;
				}
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
	private boolean scanWallRight() {
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
	 * Returns a boolean true if there's a wall in front of the robot
	 * 
	 * @return
	 */
	private boolean scanWallForward() {
		// Get the robot's current position and direction
		int[] positionData = getPosition();

		int x = positionData[0];
		int y = positionData[1];
		int direction = positionData[2];

		char[][] mapCopy = map.getGrid();

		// Get the coordinates of the supposed wall
		int[] wallCoordinates = getWallCoordinates(x, y, direction);

		int xRight = wallCoordinates[0];
		int yRight = wallCoordinates[1];

		if (mapCopy[xRight][yRight] == map.getObstacle()) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the amount of steps that the robot can move
	 * 
	 * @param forwardSpace
	 *            the free space in front of th robot.
	 * @return
	 */
	private int getSteps(int phase) {
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

		boolean stopForward = false;
		boolean stopRight = false;

		int maxDistance = 0;
		int i = 1;

		// Check the maximum forward distance
		while (i <= SCAN_RANGE && !stopForward) {
			// Get the block of this position
			char blockForward = getDistanceForward(x, y, direction, i);

			System.out.println("Forward scan - Found char: " + blockForward);

			// Block is either an obstacle or unknown
			if (blockForward == map.getObstacle()
					|| blockForward == map.getUnknown()) {
				i--;
				System.out.println("Forward scan - Found wall/unknown: " + i);
				maxDistance = i - WALL_DISTANCE;
				System.out.println("Forward scan - Max distance: "
						+ maxDistance);
				stopForward = true;
			}

			// Reached the end of the loop
			if (i == SCAN_RANGE && maxDistance == 0) {
				maxDistance = i - WALL_DISTANCE;
				stopForward = true;
			}
			i++;
		}

		if (phase == WALL) {
			i = 0;

			while (i <= SCAN_RANGE && !stopRight) {
				// Get the block of this position
				char blockRight = getDistanceRight(xRight, yRight, direction, i);

				System.out.println("Right scan - Found char: " + blockRight);

				// Check if the block's content is empty
				if (blockRight == map.getEmpty()) {
					i--;
					if (i < maxDistance) {
						maxDistance = i;
					}
					stopRight = true;
				}

				// Reached the end of the loop
				if (i == SCAN_RANGE && maxDistance == 0) {
					stopRight = true;
				}
				i++;
			}
		}
		System.out.println("Distance (steps) to travel: " + maxDistance);

		return maxDistance;
	}

	/**
	 * Maneuvers the robot around a corner of the wall
	 */
	private void turnAroundWall() {
		try {
			// Go WALL_DISTANCE forward
			moveForward(WALL_DISTANCE + 1);

			// Rotate right
			rotate(ROTATE_RIGHT);

			// Scan laser
			scanLaser();
			scanSonar();

			// Go WALL_DISTANCE + 1 forward
			moveForward(WALL_DISTANCE + 1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
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
		int direction = (int) Math.round(Double.parseDouble(parameter));
		switch (direction) {
		case 0:
			position[2] = Double.parseDouble("360");
			break;
		case 360:
			position[2] = Double.parseDouble("360");
			break;
		case 90:
			position[2] = Double.parseDouble("90");
			break;
		case 180:
			position[2] = Double.parseDouble("180");
			break;
		case 270:
			position[2] = Double.parseDouble("270");
			break;
		}
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
	 * Gets the position of the robot and scans the environment with laser.
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
	 * Gets the position of the robot and scans the environment with sonar.
	 * 
	 * @throws IOException
	 */
	private void scanSonar() throws IOException {
		robot.sendCommand("R1.GETPOS");
		this.result = input.readLine();
		parsePosition(result, position);

		robot.sendCommand("S1.SCAN");
		result = input.readLine();
		parseMeasures(result, measures);
		map.drawSonarScan(position, measures);

		// Transform 0 into 360
		if (Math.round(position[2]) == 0) {
			position[2] = RIGHT;
		}
	}

	/**
	 * Rotates the robot x degrees to the xx
	 * 
	 * @throws IOException
	 */
	private void rotate(String command) throws IOException {
		robot.sendCommand(command);
		result = input.readLine();
		rescanPosition();
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
		int directionToRight = (int) Math.round(position[2]);

		if (directionToRight == 360) {
			directionToRight = 90;
		} else {
			directionToRight += 90;
		}

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
	 * Returns the coordinates from the wall right of the robot
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
			y -= (WALL_DISTANCE + 1);
			break;
		case RIGHT:
			x += (WALL_DISTANCE + 1);
			break;
		case BOTTOM:
			y += (WALL_DISTANCE + 1);
			break;
		case LEFT:
			x -= (WALL_DISTANCE + 1);
			break;
		}

		wallCoordinates[0] = x;
		wallCoordinates[1] = y;

		return wallCoordinates;
	}

	/**
	 * Returns the current character on the OccupancyMap for the position in
	 * front of the robot
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @param i
	 * @return
	 */
	private char getDistanceForward(int x, int y, int direction, int i) {
		char distance = 0;
		char[][] mapCopy = map.getGrid();

		try {
			switch (direction) {
			case TOP:
				distance = mapCopy[x][y - i];
				break;
			case RIGHT:
				distance = mapCopy[x + i][y];
				break;
			case BOTTOM:
				distance = mapCopy[x][y + i];
				break;
			case LEFT:
				distance = mapCopy[x - i][y];
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Array out of bounds error, still continuing.");
		}
		return distance;
	}

	/**
	 * Returns the current character on the OccupancyMap for the position of the
	 * wall right of the robot
	 * 
	 * @param x
	 * @param y
	 * @param direction
	 * @param i
	 * @return
	 */
	private char getDistanceRight(int x, int y, int direction, int i) {
		char distance = 0;
		char[][] mapCopy = map.getGrid();

		try {
			switch (direction) {
			case TOP:
				distance = mapCopy[x][y - i];
				break;
			case RIGHT:
				distance = mapCopy[x + i][y];
				break;
			case BOTTOM:
				distance = mapCopy[x][y + i];
				break;
			case LEFT:
				distance = mapCopy[x - i][y];
				break;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Array out of bounds error, still continuing.");
		}
		return distance;
	}

	/**
	 * Checks if the environment is explored
	 * 
	 * @return
	 */
	private boolean isExplored() {
		// Ga over het hele bord als een unknown aan een empty grenst dan return
		// false, aan het einde return true;
		for (int i = 0; i < map.getGrid().length; i++) {
			for (int j = 0; j < map.getGrid()[0].length; j++) {
				if (map.getGrid()[i][j] == map.getUnknown()) {
					if (bordersCard(i, j, map.getEmpty())) {
						return false;
					}
				}
			}
		}
		return true;

	}

	private boolean bordersCard(int row, int column, char cardChar) {
		if (isValidRow(row - 1)) {
			if (map.getGrid()[row - 1][column] != 0) {
				// System.out.println(map.getGrid()[row -
				// 1][column].getCardChar());
				if (cardChar == map.getGrid()[row - 1][column]) {
					return true;
				}
			}
		}
		if (isValidRow(row + 1)) {
			if (map.getGrid()[row + 1][column] != 0) {
				// System.out.println(map.getGrid()[row +
				// 1][column].getCardChar());
				if (cardChar == map.getGrid()[row + 1][column]) {
					return true;
				}
			}
		}
		if (isValidColumn(column - 1)) {
			if (map.getGrid()[row][column - 1] != 0) {
				// System.out.println(map.getGrid()[row][column-1].getCardChar());
				if (cardChar == map.getGrid()[row][column - 1]) {
					return true;
				}
			}
		}
		if (isValidColumn(column + 1)) {
			if (map.getGrid()[row][column + 1] != 0) {
				// System.out.println(map.getGrid()[row][column+1].getCardChar());
				if (cardChar == map.getGrid()[row][column + 1]) {
					return true;
				}
			}

		}
		return false;
	}

	// Checks if row is on the the board
	private boolean isValidRow(int row) {
		if (row < 0) {
			return false;
		}
		if (row >= map.getGrid().length) {
			return false;
		}
		return true;
	}

	// Checks if column is on the the board
	private boolean isValidColumn(int column) {

		if (column < 0) {
			return false;
		}
		if (map.getGrid().length > 0) {
			if (column >= map.getGrid()[0].length) {
				return false;
			}
		}
		return true;
	}

}
