import java.text.DecimalFormat;

import javax.swing.JLabel;

public class SpaceCraft {

	public SpaceCraftStatus status; // Off or On
	private Point location;
	private CPU engine;
	public Timer timer;
	private PID pid;

	public double vs; // Vertical speed
	public double hs; // Horizontal speed
	public double dist; // Distance from the moon
	public double ang; // Angle to moon
	public double alt; // Altitude
	public double lat; // Altitude
	public double acc; // Acceleration
	public double fuel;
	public double weight;
	public double NN;

	SpaceCraft() {
		this.timer = new Timer();
		this.engine = new CPU(200, "update");
		this.engine.addFunction(this::update);
		this.location = new Point(0, 0);
		this.status = SpaceCraftStatus.ON;
		this.pid = new PID(1000);
		vs = 24.8;
		hs = 932; 
		dist = Config.START_DISTANCE;
		ang = 58.3; // zero is vertical (as in landing)
		alt = Config.START_ALTITUDE; // 2:25:40 (as in the simulation) // https://www.youtube.com/watch?v=JJ0VfRL9AMs
		lat = 10000;
		timer.time = 0;
		timer.dt = 1; // sec
		acc = 0; // Acceleration rate (m/s^2)
		fuel = 121; //
		weight = Config.WEIGHT_EMP + fuel;
		NN = 0.7; // rate[0,1] -> Normalize the fuel according to the spaceCraft situation
	}

	public void update(int deltaTime) {
		if (this.status == SpaceCraftStatus.ON) {
			// over 2 kilometer above the ground
			// Braking
			if (alt > 2000) { // maintain a vertical speed of [20-25] meter/second
				if (vs > 25) {
					NN += 0.003 * timer.dt;
				} // more power for braking
				if (vs < 20) { 
					NN -= 0.003 * timer.dt;
				} // less power for braking
			}
			// lower than 2 km - horizontal speed should be close to zero
			else {
				if (ang > 3) {
					ang -= 3;
				} // rotate to vertical position.
				else {
					ang = 0;
				}
				NN = 0.5; // brake slowly, a proper PID controller here is needed!
				if (hs < 2) {
					hs = 0;
				}
				if (alt < 125) { // very close to the ground!
					NN = 1; // maximum braking!
					if (vs < 5) {
						NN = 0.7;
					} // if it is slow enough - go easy on the brakes
				}
			}
			if (alt < 5) { // no need to stop
				NN = 0.4;
			}
			// main computations
			double ang_rad = Math.toRadians(ang);
			double h_acc = Math.sin(ang_rad) * acc;
			double v_acc = Math.cos(ang_rad) * acc;
			double vacc = Utils.getAcc(hs);
			//optional
			double hacc = Utils.getAcc(vs);
			//
			timer.time += timer.dt;
			double dw = timer.dt * Config.ALL_BURN * NN;
			if (fuel > 0) {
				fuel -= dw;
				weight = Config.WEIGHT_EMP + fuel;
				acc = NN * Utils.accMax(weight);
			} else { // ran out of fuel
				acc = 0;
			}

			v_acc -= vacc;
			if (hs > 0) {
				hs -= h_acc * timer.dt;
				//optional
				h_acc -= hacc;
				//
			}
			dist -= hs * timer.dt;
			vs -= v_acc * timer.dt;
			alt -= vs * timer.dt;
			//optional 
			hs -= h_acc * timer.dt;
			lat -= hs * timer.dt;
			//
			setLocation(new Point(10000 - (int)lat, (Config.START_ALTITUDE - (int) alt)));
			
			if (alt <= 0)
				this.status = SpaceCraftStatus.OFF;
			
		}
	}

	public synchronized void land() {
		this.engine.play();
	}

	public void updateInfo(int deltaTime , JLabel info) {
		if(timer.time % 50 == 0) {
		DecimalFormat dfff = new DecimalFormat("#.##");
		info.setText("time : " + dfff.format(timer.time) + ", vs : " + dfff.format(vs) + ", hs : "
						+ dfff.format(hs) + ", distance : " + dfff.format(dist) +  ", alt : " + dfff.format(alt)
						+ ", ang : " + dfff.format(ang) + ", weight : " + dfff.format(weight) + ", acc : "
						+ dfff.format(acc));
		}
	}
	
	public PID getPID() {
		return this.pid;
	}

	public CPU getCPU() {
		return this.engine;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return this.location;
	}

	public static enum SpaceCraftStatus {
		OFF, ON
	}
}
