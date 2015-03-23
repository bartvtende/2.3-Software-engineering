package model.device;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import model.environment.Environment;
import model.environment.Obstacle;
import model.environment.Position;
import model.robot.MobileRobot;

/**
 * Title : The Mobile Robot Explorer Simulation Environment v2.0 Copyright: GNU
 * General Public License as published by the Free Software Foundation Company :
 * Hanze University of Applied Sciences
 *
 * @author Dustin Meijer (2012)
 * @author Alexander Jeurissen (2012)
 * @author Davide Brugali (2002)
 * @version 2.0
 */

public class Sonar extends ScanDevice {

	public double rotStep = 360;
	
	public Sonar(String name, MobileRobot robot, Position localPos,
			Environment environment) {
		super(name, robot, localPos, environment);

		this.detect = false;
		this.scan = false;

		backgroundColor = Color.cyan;
		this.addPoint(0, 2);
		this.addPoint(100, 2);
		this.addPoint(100, -2);
		this.addPoint(0, -2);
	}

	@Override
	public void executeCommand(String command) {
		if (command.contains("ROTATETO")) {
			this.rotStep = 4.0;
			double direction = Math.abs(Double.parseDouble(command.trim()
					.substring(9).trim()));

			while (direction < 0.0)
				direction += 360.0;
			while (direction > 360.0)
				direction -= 360.0;
			double dirDiff = direction - Math.toDegrees(localPosition.getT()); // ??????????????
			if (dirDiff >= 0.0 && dirDiff <= 180.0) {
				this.numSteps = dirDiff / rotStep;
				this.orientation = CLOCKWISE;
			} else if (dirDiff >= 0.0 && dirDiff > 180.0) {
				this.numSteps = (360.0 - dirDiff) / rotStep;
				this.orientation = ANTICLOCKWISE;
			} else if (dirDiff < 0.0 && -dirDiff <= 180.0) {
				this.numSteps = -dirDiff / rotStep;
				this.orientation = ANTICLOCKWISE;
			} else if (dirDiff < 0.0 && -dirDiff > 180.0) {
				this.numSteps = (360.0 + dirDiff) / rotStep;
				this.orientation = CLOCKWISE;
			}
			this.executingCommand = true;
		} else if (command.equalsIgnoreCase("READ")) {
			writeOut("t=" + Double.toString(this.localPosition.getT()) + " d="
					+ Double.toString(this.read(true, true)));
			// ??????????????
		} else if (command.equalsIgnoreCase("SCAN")) {
			this.rotStep = 1.0;
			this.scanMeasurements.clear();
			this.numSteps = 360.0 / rotStep;
			this.orientation = CLOCKWISE;
			this.scan = true;
			// send the list of measures
			this.commands.add("GETMEASURES");
			this.executingCommand = true;
		} else if (command.equalsIgnoreCase("GETMEASURES")) {
			Measurement measure;
			String measures = "SCAN";
			for (Measurement scanMeasure : scanMeasurements) {
				measure = scanMeasure;
				measures += " d=" + measure.distance + " t="
						+ measure.direction;
			}
			writeOut(measures);
		} else if (command.equalsIgnoreCase("DETECT")) {
			detect = true;
			rotStep = 8.0;
			if (detectMeasure != null) {
				writeOut("LASER DETECT d=" + detectMeasure.distance + " t="
						+ detectMeasure.direction);
				detectMeasure = null;
			} else if (localPosition.getT() == Math.toRadians(45.0)) { // ?????????????
				// move the laser to the left position
				commands.add("ROTATETO 315");
				// repeats this command
				commands.add("DETECT");
			} else if (localPosition.getT() == Math.toRadians(315.0)) { // ??????????????
				// move the laser to the right position
				commands.add("ROTATETO 45");
				// repeats this command
				commands.add("DETECT");
			} else {
				// move the laser to the right position
				commands.add("ROTATETO 45");
				// repeats this command
				commands.add("DETECT");
			}
		} else
			writeOut("DECLINED");
	}

	@Override
	public double read(boolean first, boolean opaque) {
		Point2D centre = new Point2D.Double(localPosition.getX(), localPosition.getY());
        Point2D front = new Point2D.Double(localPosition.getX() + RANGE * Math.cos(localPosition.getT()),
                localPosition.getY() + RANGE * Math.sin(localPosition.getT()));
        // reads the robot's position
        robot.readPosition(robotPosition);
        // center's coordinates according to the robot position
        robotPosition.rotateAroundAxis(centre);
        // front's coordinates according to the robot position
        robotPosition.rotateAroundAxis(front);

        double minDistance = -1.0;
        for (int i = 0; i < environment.getObstacles().size(); i++) {
            // This is really dirty: the laser uses direct access to environment's obstacles
            Obstacle obstacle = environment.getObstacles().get(i);
            if (obstacle.getOpaque()) {
                double dist = pointToObstacle(obstacle.getPolygon(), centre, front, first);
                if (minDistance == -1.0 || (dist > 0 && dist < minDistance)) {
                    minDistance = dist;
                    if (minDistance > -1 && first) {
                        return minDistance;
                    }
                }
            }
        }
        if (minDistance > 0)
            return minDistance;
        return -1.0;
	}
}
