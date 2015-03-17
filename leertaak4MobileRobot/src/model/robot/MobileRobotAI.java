	package model.robot;

import model.device.Device;
import model.device.Laser;
import model.virtualmap.OccupancyMap;

import java.io.PipedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PipedOutputStream;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Title    :   The Mobile Robot Explorer Simulation Environment v2.0
 * Copyright:   GNU General Public License as published by the Free Software Foundation
 * Company  :   Hanze University of Applied Sciences
 *
 * @author Dustin Meijer        (2012)
 * @author Alexander Jeurissen  (2012)
 * @author Davide Brugali       (2002)
 * @version 2.0
 */

public class MobileRobotAI implements Runnable {

	private static final int DISTANCE_FROM_WALL = 20;
	
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
				this.output = new PrintWriter(new PipedOutputStream(pipeIn), true);

				robot.setOutput(output);
				boolean foundNewWall = false;
				
				// Step 1: Scan the surroundings with the laser and save the results
				scanLaser();
				
				// Step 2: Check the map and determine if the robot is following the wall or not
				boolean foundWall = findMovement();
				
				// Step 3: Determine the move
				if (foundWall) {
					moveForward(getSteps()); // Calculate the amount of steps and go forward
				} else {
					while (!foundNewWall) {
						// Try to find a guiding wall
						moveRight(); // Rotate right (to find the wall)
						foundNewWall = findMovement();
						moveForward(getSteps()); // Calculate the amount of steps and go forward
						scanLaser();
					}
				}
				
				// Step 4: Check if the exploration is completed
				isCompleted();
			} catch (IOException ioe) {
				System.err.println("execution stopped");
				running = false;
			}
		}
		System.out.println("Robot is now shutting down..");
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
			value = value.substring(5);  // removes the "SCAN " keyword

			StringTokenizer tokenizer = new StringTokenizer(value, " ");

			double distance;
			int direction;
			while (tokenizer.hasMoreTokens()) {
				distance = Double.parseDouble(tokenizer.nextToken().substring(2));
				direction = (int) Math.round(Math.toDegrees(Double.parseDouble(tokenizer.nextToken().substring(2))));
				if (direction == 360) {
					direction = 0;
				}
				measures[direction] = distance;
				// Printing out all the degrees and what it encountered.
				//System.out.println("Direction = " + direction + " - Distance = " + distance);
			}
		}
	}
	
	/**
	 * Gets the position of the robot and scans the environment.
	 */
	private void scanLaser() {
		try {
			robot.sendCommand("R1.GETPOS");
			this.result = input.readLine();
			parsePosition(result, position);
	
			robot.sendCommand("L1.SCAN");
			result = input.readLine();
			parseMeasures(result, measures);
			map.drawLaserScan(position, measures);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Rotates the robot 90 degrees to the right
	 */
	private void moveRight() {
        robot.sendCommand("P1.ROTATERIGHT 90");
        try {
			result = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
       
        // TODO: Refactor with findMovement()
        
        // Update the direction after the rotation to the right
        int newDirection = (int) Math.round(position[2]);

		if (newDirection % 360 == 0) 
			newDirection -= 360;
		newDirection += 90;
        
        this.position[2] = (double) newDirection;
	}

	/**
	 * Moves the robot forward for a x amount of steps
	 * 
	 * @param steps
	 */
	private void moveForward(int steps) {
		steps *= 10;
        this.robot.sendCommand("P1.MOVEFW " + steps);
        try {
			result = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns true if the robot is currently following the wall
	 * 
	 * @return
	 */
	private boolean findMovement() {
		boolean foundWall = false;
		
		// TODO: REFACTOR THIS BITCH
		
		// Get the robot's current position
		int xPosition = (int) Math.round(position[0]);
		int yPosition = (int) Math.round(position[1]);
		
		// Get the robot's current facing direction
		int direction = (int) Math.round(position[2]);
				
		// Get the position to the right
		int directionToRight = direction;
		if (direction % 360 == 0) 
			directionToRight -= 360;
		directionToRight += 90;
		
		// Compare the position and direction to the OccupancyMap		
		int xPositionRight = xPosition;
		int yPositionRight = yPosition;
		
		// Get the possible wall position on the grid
		switch (directionToRight) {
			case 90:
				yPositionRight += DISTANCE_FROM_WALL;
				break;
			case 180:
				xPositionRight -= DISTANCE_FROM_WALL;
				break;
			case 270:
				yPositionRight -= DISTANCE_FROM_WALL;
				break;
			case 360:
				xPositionRight += DISTANCE_FROM_WALL;
				break;
		}
		

		// Determine whether or not the robot is following the wall
		if (map.getGrid()[xPositionRight / 10][yPositionRight / 10] == map.getObstacle()) {
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
	 * @param forwardSpace the free space in front of th robot. 
	 * @return
	 */
	private int getSteps() {
		int amountOfSteps;
		int laserRange = 0;

		// Get the robot's current position
		int xPosition = ((int) Math.round(position[0])) / 10;
		int yPosition = ((int) Math.round(position[1])) / 10;
		
		// Get the robot's current facing direction
		int direction = (int) Math.round(position[2]);
		
		// Check the new position for the right wall
		int xPositionRight = xPosition;
		int yPositionRight = yPosition;
		
		switch (direction) {
			case 90:
				xPositionRight -= (DISTANCE_FROM_WALL / 10);
				break;
			case 180:
				yPositionRight -= (DISTANCE_FROM_WALL / 10);
				break;
			case 270:
				xPositionRight += (DISTANCE_FROM_WALL / 10);
				break;
			case 360:
				yPositionRight += (DISTANCE_FROM_WALL / 10);
				break;
		}
		
		// Boolean that checks if a wall has been found
		boolean foundWall = false;
		
		// Scan the surroundings in front of robot
		for (Device devices : robot.getSensors()) {
			if (devices instanceof Laser) {
				laserRange = ((Laser) devices).getRange();
			}
		}
		
		amountOfSteps = laserRange;
		
		// Check the environment in front of the robot
		for (int i = 0; i < (laserRange / 10); i++) {
			int newAmountOfSteps;
			char nextCharForward = 0;
			char nextCharWall = 0;
			switch (direction) {
				case 90:
					nextCharForward = map.getGrid()[xPosition][yPosition+i];
					nextCharWall = map.getGrid()[xPositionRight][yPositionRight+i];
					break;
				case 180:
					nextCharForward = map.getGrid()[xPosition-i][yPosition];
					nextCharWall = map.getGrid()[xPositionRight-i][yPositionRight];
					break;
				case 270:
					nextCharForward = map.getGrid()[xPosition][yPosition-i];
					nextCharWall = map.getGrid()[xPositionRight][yPositionRight-i];
					break;
				case 360:
					nextCharForward = map.getGrid()[xPosition+i][yPosition];
					nextCharWall = map.getGrid()[xPositionRight+i][yPositionRight];
					break;
			}
			if (nextCharForward == map.getObstacle() || nextCharForward == map.getUnknown()) {
				newAmountOfSteps = i - (DISTANCE_FROM_WALL / 10);
				if (amountOfSteps > newAmountOfSteps) {
					amountOfSteps = newAmountOfSteps;
				}
			}
			if (nextCharWall != map.getObstacle()) {
				if (nextCharWall == map.getUnknown()) {
					newAmountOfSteps = i - (DISTANCE_FROM_WALL / 10);
					if (amountOfSteps > newAmountOfSteps) {
						amountOfSteps = newAmountOfSteps;
					}
				}
				if (nextCharWall == map.getEmpty()) {
					if (foundWall) {
						newAmountOfSteps = i + (DISTANCE_FROM_WALL / 10);
						if (amountOfSteps > newAmountOfSteps) {
							amountOfSteps = newAmountOfSteps;
						}
					}
				}
			} else {
				foundWall = true;
			}
		}
		amountOfSteps /= 10;
		System.out.println(amountOfSteps);
		
		return amountOfSteps;
	}
	
	/**
	 * Checks if all the walls are explored and stop the robot
	 */
	private void isCompleted() {
		// Check if all the walls in the OccupancyMap are fully explored
		boolean explored = false;

		// If yes, "Park" the robot and set running to false
		if (explored) {
			this.running = false;
		}
	}

}
