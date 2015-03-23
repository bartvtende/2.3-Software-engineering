package model.device;

import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import model.environment.Environment;
import model.environment.Obstacle;
import model.environment.Position;
import model.robot.MobileRobot;

public abstract class ScanDevice extends Device {

	// Maximum range of the scan device
	public static final int RANGE = 100;

	public static final int CLOCKWISE = 1;
	public static final int ANTICLOCKWISE = -1;
	
	public int orientation = 1;
	public double rotStep = 1.0;     // one degree
	public double numSteps = 0;
	
	public double rad = 0.0;
	
	public Position robotPos = new Position();
	public Position localPos;
	
	public boolean detect;
	public boolean scan;
	
	protected ScanDevice(String name, MobileRobot robot, Position local,
			Environment environment) {
		super(name, robot, local, environment);
	}
	
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
			if (obstacle.getOpaque() && opaque) {
				double dist = pointToObstacle(obstacle.getPolygon(), centre, front, first);
				if (minDistance == -1.0 || (dist > 0 && dist < minDistance)) {
					minDistance = dist;
					if (minDistance > -1 && first) {
						return minDistance;
					}
				}
			} else if (!obstacle.getOpaque() && !opaque) {
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

	// receives the vertex coordinates of segment beam;
	// if segment beam intersects an edge of this PhysicalShape, it returns
	// the distance of the first vertex of beam from the closest edge
	// if beam does not intersect the PhysicalShape, the return value is -1.0
	public double pointToObstacle(Polygon polygon, Point2D centre, Point2D front, boolean first) {
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
					py = (front.getY() - centre.getY()) / (front.getX() - centre.getX()) * (px - centre.getX()) + centre.getY();
				} else {
					m1 = (y2 - y1) / (x2 - x1);
					q1 = y1 - m1 * x1;
					m2 = (front.getY() - centre.getY()) / (front.getX() - centre.getX());
					q2 = centre.getY() - m2 * centre.getX();
					px = (q2 - q1) / (m1 - m2);
					py = m1 * px + q1;
				}
				// calculates the distance between (cx, cy) and the intersection point
				dist = Point2D.Double.distance(centre.getX(), centre.getY(), px, py);
				if (minDistance == -1.0 || minDistance > dist)
					minDistance = dist;
				if (first && minDistance > 0.0)
					return minDistance;
			}
		}
		return minDistance;
	}
	
	public abstract void executeCommand(String command);
	
	public abstract void nextStep();
}
