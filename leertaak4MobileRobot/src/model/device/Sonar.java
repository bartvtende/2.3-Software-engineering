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

	private Measurement detectMeasure;
	private final ArrayList<Measurement> scanMeasurements;

	public Sonar(String name, MobileRobot robot, Position localPos,
			Environment environment) {
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

	@Override
	public void executeCommand(String command) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextStep() {
		// TODO Auto-generated method stub
		
	}
}
