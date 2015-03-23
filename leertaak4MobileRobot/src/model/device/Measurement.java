package model.device;

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
public class Measurement {

	protected double distance;
	protected double direction;

	protected Measurement(double distance, double direction) {
		this.set(distance, direction);
		this.processDirectionValue();
	}

	protected void set(double distance, double direction) {
		this.distance = distance;
		this.direction = direction;
	}

	private void processDirectionValue() {
		while (direction >= 2.0 * Math.PI)
			direction -= 2.0 * Math.PI;
		while (direction < 0.0)
			direction += 2.0 * Math.PI;
	}

}
