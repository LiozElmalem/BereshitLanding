import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.JLabel;

public class SpaceCraft {
 
	private SpaceCraftStatus status; // Off / On
	private ArrayList<Engine> engines;
	private Point location;
	private CPU cpu;
	public Timer timer;
	private PID pid;

	public double vs; // Vertical speed
	public double hs; // Horizontal speed
	public double dist; // Distance from the moon
	public double ang; // Angle to moon
	public double alt; // Altitude
	public double acc; // Acceleration
	public double fuel;
	public double weight;
	public double NN;

	SpaceCraft() {
		this.timer = new Timer();
		this.cpu = new CPU(200, "update");
		this.cpu.addFunction(this::update2);
		this.engines = new ArrayList<Engine>(8);
		this.location = Config.startPoint;
		this.status = SpaceCraftStatus.ON;
		this.pid = new PID(1000);
		double engineInitPower = 1;
		for(int i = 0; i < 8; i++) {
			Engine engine = null;
			switch(i) {
			case 1:
				engine = new Engine("LeftEast1" , engineInitPower);
				break;
			case 2:
				engine = new Engine("LeftEast2" , engineInitPower);
				break;
			case 3:
				engine = new Engine("LeftWest1" , engineInitPower);
				break;
			case 4:
				engine = new Engine("LeftWest2" , engineInitPower);
				break;
			case 5:
				engine = new Engine("RightEast1" , engineInitPower);
				break;
			case 6:
				engine = new Engine("RightEast2" , engineInitPower);
				break;
			case 7:
				engine = new Engine("RightWest1" , engineInitPower);
				break;
			case 8:
				engine = new Engine("RightWest2" , engineInitPower);
				break;
			}
			engines.add(engine);
		}
		vs = 24.8;
		hs = 932; 
		dist = Config.START_DISTANCE;
		ang = 58.3; // zero is vertical (as in landing)
		alt = Config.START_ALTITUDE; // 2:25:40 (as in the simulation) // https://www.youtube.com/watch?v=JJ0VfRL9AMs
		timer.time = 0;
		timer.dt = 1; // sec
		acc = 0; // Acceleration rate (m/s^2)
		fuel = 121;  
		weight = Config.WEIGHT_EMP + fuel;
		NN = 0.7; // rate[0,1] -> Normalize the fuel according to the spaceCraft situation
	}
	
	public void update2(int deltaTime) {
		if(status == SpaceCraftStatus.ON) {
			
			NN = pid.control(timer.dt, NN);
			ang = pid.control(timer.dt, ang);
			hs = pid.control(timer.dt , hs);
			acc = pid.control(timer.dt, acc);
			alt = pid.control(timer.dt, alt);
			vs = pid.control(timer.dt, vs);
			dist = pid.control(timer.dt, dist);
			
			double ang_rad = Math.toRadians(ang);
			double h_acc = Math.sin(ang_rad) * acc;
			double v_acc = Math.cos(ang_rad) * acc;
			double vacc = Utils.getAcc(hs);
			double dw = timer.dt * Config.ALL_BURN * NN;
			
			timer.time += timer.dt;

//			if (fuel > 0) {
//				fuel -= dw;
//				weight = Config.WEIGHT_EMP + fuel;
//				acc = NN * Utils.accMax(weight);
//			} else { // ran out of fuel
//				acc = 0;
//			}
//			v_acc -= vacc;
//			if (hs > 0) {
//				hs -= h_acc * timer.dt;
//			}
			
			dist -= hs * timer.dt;
			vs -= v_acc * timer.dt;
			alt -= vs * timer.dt;
			
			double distToTarget = Utils.getDistanceBetweenPoints(location, Config.target);
			
			move();
			 
			if(Config.moonCircleBounds.contains(location.x , location.y) || distToTarget < 1) {
				this.status = SpaceCraftStatus.OFF;
			}
		}
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
				
//				NN = 0.5; // brake slowly, a proper PID controller here is needed!
				NN = pid.control(timer.dt, 0.5);
				
				if(hs < 2) {
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
			double dw = timer.dt * Config.ALL_BURN * NN;

			timer.time += timer.dt;
			
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
			}
			
			dist -= hs * timer.dt;
			vs -= v_acc * timer.dt;
			alt -= vs * timer.dt;
			
			double distToTarget = Utils.getDistanceBetweenPoints(location, Config.target);
			
			move();
			 
			if(Config.moonCircleBounds.contains(location.x , location.y) || distToTarget < 1) {
				this.status = SpaceCraftStatus.OFF;
			}

		}
	}

	public synchronized void land() {
		this.cpu.play();
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
		return this.cpu;
	}

	public void setLocation(Point location) {
		this.location = location;
	}

	public Point getLocation() {
		return this.location;
	}
	
	public void move() {
		setLocation(new Point(990, (Config.START_ALTITUDE - (int) alt) / Config.NORM_ALT));
	}

	public static enum SpaceCraftStatus {
		OFF, ON , CLOSE
	}
}
