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
	 * In this method the gui.controller sends commands to the robot and its devices.
	 * At the moment all the commands are hardcoded.
	 * The exercise is to let the gui.controller make intelligent decisions based on
	 * what has been discovered so far. This information is contained in the OccupancyMap.
	 */
	public void run() {
		this.running = true;
		this.position = new double[3];
		this.measures = new double[360];
		while (running) {
			try {
				this.result = "";

				this.pipeIn = new PipedInputStream();
				this.input = new BufferedReader(new InputStreamReader(pipeIn));
				this.output = new PrintWriter(new PipedOutputStream(pipeIn), true);

				robot.setOutput(output);
				
				// Step 1: Scan the surroundings with the laser and save the results
				scanLaser();
				
				// Step 2: Check the map and determine if the robot is following the wall or not
				boolean foundWall = findMovement();
				
				// Step 3: Determine the move
				if (foundWall) {
					moveForward(getSteps()); // Calculate the amount of steps and go forward
				} else {
					boolean foundNewWall = false;
					// Try to find a guiding wall
					while(!foundWall) {
						moveRight(); // Rotate right (to follow the wall)
						foundWall = findMovement();
						if (foundWall) {
							foundNewWall = true;
						}
					}
					moveForward(getSteps()); // Calculate the amount of steps and go forward
				}
				
				// Step 4: Check if the exploration is completed
				isCompleted();
				this.running = false;
			} catch (IOException ioe) {
				System.err.println("execution stopped");
				running = false;
			}
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
				System.out.println("direction = " + direction + " distance = " + distance);
			}
		}
	}
	
	private findPosition
	
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
	private void moveLeft() {
        robot.sendCommand("P1.ROTATELEFT 90");
        try {
			result = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void moveRight() {
        robot.sendCommand("P1.ROTATERIGHT 90");
        try {
			result = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void moveForward(int steps) {
		steps *= 10;
        this.robot.sendCommand("P1.MOVEFW " + steps);
        try {
			result = input.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
