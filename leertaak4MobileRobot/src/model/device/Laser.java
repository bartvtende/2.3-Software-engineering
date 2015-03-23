package model.device;

import model.environment.Environment;
import model.environment.Obstacle;
import model.environment.Position;
import model.robot.MobileRobot;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

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

public class Laser extends ScanDevice {
	
	private Measurement detectMeasure;
	private final ArrayList<Measurement> scanMeasurements;

	public Laser(String name, MobileRobot robot, Position localPos, Environment environment) {
		super(name, robot, localPos, environment);

		this.detect = false;
		this.scan = false;

		this.scanMeasurements = new ArrayList<Measurement>();

		backgroundColor = Color.cyan;
		this.addPoint(0, 2);
		this.addPoint(100, 2);
		this.addPoint(100, -2);
		this.addPoint(0, -2);
	}	

	public void executeCommand(String command) {
		if (command.contains("ROTATETO")) {
			this.rotStep = 4.0;
			double direction = Math.abs(Double.parseDouble(command.trim().substring(9).trim()));

			while (direction < 0.0)
				direction += 360.0;
			while (direction > 360.0)
				direction -= 360.0;
			double dirDiff = direction - Math.toDegrees(localPosition.getT());   // ??????????????
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
			writeOut("t=" + Double.toString(this.localPosition.getT()) + " d=" + Double.toString(this.read(true, false)));
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
				measures += " d=" + measure.distance + " t=" + measure.direction;
			}
			writeOut(measures);
		} else if (command.equalsIgnoreCase("DETECT")) {
			detect = true;
			rotStep = 8.0;
			if (detectMeasure != null) {
				writeOut("LASER DETECT d=" + detectMeasure.distance + " t=" + detectMeasure.direction);
				detectMeasure = null;
			} else if (localPosition.getT() == Math.toRadians(45.0)) {   // ?????????????
				// move the laser to the left position
				commands.add("ROTATETO 315");
				// repeats this command
				commands.add("DETECT");
			} else if (localPosition.getT() == Math.toRadians(315.0)) {  // ??????????????
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


	public void nextStep() {
		if (this.executingCommand && numSteps > 0.0) {
			if (numSteps < 1.0) {
				localPosition.rotateAroundAxis(0.0, 0.0, orientation * numSteps * rotStep);
			} else {
				localPosition.rotateAroundAxis(0.0, 0.0, orientation * rotStep);
			}
			environment.processEvent(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			numSteps -= 1.0;
			this.executingCommand = true;

		} else if (this.executingCommand) {
			this.executingCommand = false;
			if (!detect && !scan) {
				writeOut("LASER ARRIVED");
			}

		}

		if (detect) {
			double distance = this.read(true, false);
			if (distance > -1.0) {
				if (detectMeasure == null) {
					detectMeasure = new Measurement(distance, localPosition.getT());  // ?????????????
				} else if (detectMeasure.distance > distance) {
					detectMeasure.set(distance, localPosition.getT());  // ????????????
				}
			}
		} else if (scan) {
			double distance = this.read(false, false);
			if (distance > -1.0) {
				scanMeasurements.add(new Measurement(distance, localPosition.getT()));  // ??????????????
			}
		}
	}
	
	public int getRange() {
		return ScanDevice.RANGE;
	}
}
