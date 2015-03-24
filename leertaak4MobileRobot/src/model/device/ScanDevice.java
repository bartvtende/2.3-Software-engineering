package model.device;

import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import model.environment.Environment;
import model.environment.Position;
import model.robot.MobileRobot;

public abstract class ScanDevice extends Device {

	// Maximum range of the scan device
	public static final int RANGE = 100;

	public static final int CLOCKWISE = 1;
	public static final int ANTICLOCKWISE = -1;

	protected Measurement detectMeasure;
	protected final ArrayList<Measurement> scanMeasurements;

	public int orientation = 1;
	public double rotStep = 1.0; // one degree
	public double numSteps = 0;

	public double rad = 0.0;

	public Position robotPos = new Position();
	public Position localPos;

	public boolean detect;
	public boolean scan;

	protected ScanDevice(String name, MobileRobot robot, Position local,
			Environment environment) {
		super(name, robot, local, environment);

		this.scanMeasurements = new ArrayList<Measurement>();
	}

	public abstract double read(boolean first);

	// receives the vertex coordinates of segment beam;
	// if segment beam intersects an edge of this PhysicalShape, it returns
	// the distance of the first vertex of beam from the closest edge
	// if beam does not intersect the PhysicalShape, the return value is -1.0
	public double pointToObstacle(Polygon polygon, Point2D centre,
			Point2D front, boolean first) {
		int j;
		double minDistance = -1.0;
		double dist;
		double px, py;
		double x1, y1, x2, y2;
		double m1, q1, m2, q2;
		Line2D.Double beam = new Line2D.Double(centre, front);

		for (int i = 0; i < polygon.npoints; i++) {
			j = i + 1;
			if (j == polygon.npoints)
				j = 0;
			x1 = polygon.xpoints[i];
			y1 = polygon.ypoints[i];
			x2 = polygon.xpoints[j];
			y2 = polygon.ypoints[j];
			if (beam.intersectsLine(x1, y1, x2, y2)) {
				// calculates the intersection point
				if (centre.getX() == front.getX()) {
					px = centre.getX();
					py = (y2 - y1) / (x2 - x1) * (px - x1) + y1;
				} else if (x1 == x2) {
					px = x1;
					py = (front.getY() - centre.getY())
							/ (front.getX() - centre.getX())
							* (px - centre.getX()) + centre.getY();
				} else {
					m1 = (y2 - y1) / (x2 - x1);
					q1 = y1 - m1 * x1;
					m2 = (front.getY() - centre.getY())
							/ (front.getX() - centre.getX());
					q2 = centre.getY() - m2 * centre.getX();
					px = (q2 - q1) / (m1 - m2);
					py = m1 * px + q1;
				}
				// calculates the distance between (cx, cy) and the intersection
				// point
				dist = Point2D.Double.distance(centre.getX(), centre.getY(),
						px, py);
				if (minDistance == -1.0 || minDistance > dist)
					minDistance = dist;
				if (first && minDistance > 0.0)
					return minDistance;
			}
		}
		return minDistance;
	}

	public abstract void executeCommand(String command);

	public void nextStep() {
		if (this.executingCommand && numSteps > 0.0) {
			if (numSteps < 1.0) {
				localPosition.rotateAroundAxis(0.0, 0.0, orientation * numSteps
						* rotStep);
			} else {
				localPosition.rotateAroundAxis(0.0, 0.0, orientation * rotStep);
			}
			environment.processEvent(new ActionEvent(this,
					ActionEvent.ACTION_PERFORMED, null));
			numSteps -= 1.0;
			this.executingCommand = true;

		} else if (this.executingCommand) {
			this.executingCommand = false;
			if (!detect && !scan) {
				writeOut("LASER ARRIVED");
			}

		}

		if (detect) {
			double distance = this.read(true);
			if (distance > -1.0) {
				if (detectMeasure == null) {
					detectMeasure = new Measurement(distance,
							localPosition.getT());
				} else if (detectMeasure.distance > distance) {
					detectMeasure.set(distance, localPosition.getT());
				}
			}
		} else if (scan) {
			double distance = this.read(false);
			if (distance > -1.0) {
				scanMeasurements.add(new Measurement(distance, localPosition
						.getT()));
			}
		}
	}
	
	public int getRange(){
		return RANGE;
	}
}
