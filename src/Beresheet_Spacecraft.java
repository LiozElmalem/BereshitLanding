import java.text.DecimalFormat;

public class Beresheet_Spacecraft {
	
	public static final double WEIGHT_EMP = 165; // kg
	public static final double WEIGHT_FULE = 420; // kg
	public static final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FULE; // kg
	public static final double MAIN_ENG_F = 430; // N
	public static final double SECOND_ENG_F = 25; // N
	public static final double MAIN_BURN = 0.15; // liter per sec, 12 liter per m'
	public static final double SECOND_BURN = 0.009; // liter per sec 0.6 liter per m'
	public static final double ALL_BURN = MAIN_BURN + 8 * SECOND_BURN;

	private double vs;
	private double hs;
	private double dist;
	private double ang; // zero is vertical (as in landing)
	private double alt;
	private double lat;
	private double time;
	private double dt; // seconds
	private double acc; // Acceleration rate (m/s^2)
	private double fuel; //
	private double weight;
	private double NN; // rate[0,1] How much gas to give to the system

	private PID pid; // NN one
	private NavigationEngine[] EngArr;
	private Point location;

	public Beresheet_Spacecraft() {
		vs = 24.8;
		hs = 932;
		dist = 181 * 1000;
		ang = 58.3;
		alt = 13748; // 30 k"m
		lat = 0;
		time = 0;
		dt = 1;
		acc = 0;
		fuel = 121;
		weight = WEIGHT_EMP + fuel;
		
		pid = new PID(0.7 , 0.01 , 0.01 , 1 , 0);
		NN = 0.7;

		location = new Point(0, 100);// starting point

		EngArr = new NavigationEngine[8];
		EngArr[0] = new NavigationEngine("North1", 0);
		EngArr[1] = new NavigationEngine("North2", 0);
		EngArr[2] = new NavigationEngine("East1", 0);
		EngArr[3] = new NavigationEngine("East2", 0);
		EngArr[4] = new NavigationEngine("South1", 0);
		EngArr[5] = new NavigationEngine("South2", 0);
		EngArr[6] = new NavigationEngine("West1", 0);
		EngArr[7] = new NavigationEngine("West2", 0);
	}

	/////////// Get//////////////////////////////
	public double getVS() {
		return vs;
	}

	public double getHS() {
		return hs;
	}

	public double getDist() {
		return dist;
	}

	public double getAng() {
		return ang;
	}

	public double getAlt() {
		return alt;
	}

	public double getTime() {
		return time;
	}

	public double getDT() {
		return dt;
	}

	public double getAcc() {
		return acc;
	}

	public double getFuel() {
		return fuel;
	}

	public double getWeight() {
		return weight;
	}

	public double getNN() {
		return NN;
	}

	public Point getLocation() {
		return location;
	}

	//////// Set////////////////////////////////
	public void setVS(double vs) {
		this.vs = vs;
	}

	public void setHS(double hs) {
		this.hs = hs;
	}

	public void setDist(double d) {
		this.dist = d;
	}

	public void setAng(double a) {
		this.ang = a;
	}

	public void setAlt(double alt) {
		this.alt = alt;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setTime(double t) {
		this.time = t;
	}

	public void setDT(double dt) {
		this.dt = dt;
	}

	public void setAcc(double acc) {
		this.acc = acc;
	}

	public void setFuel(double f) {
		this.fuel = f;
	}

	public void setWeight(double w) {
		this.weight = w;
	}

	public void setNN(double nn) {
		this.NN = nn;
	}

	public void setLocation(double x, double y) {
		this.location.x = x;
		this.location.y = y;
	}

	public void updateAllEnginesPower(double z) {
		for (int i = 0; i < EngArr.length; i++) {
			EngArr[i].setPower(z);
		}
	}

	public void timer() {
		time = (time + dt);
	}

	public void loactionUpdate() {
		alt = (alt - dt * vs); // y
		lat = (lat + dt * hs); // x
		dist = (new Point(lat, alt).distance2D(Moon.realDestinationPoint));
	}

	public void speedControl(double h_acc, double v_acc) {
		if (hs > 0) 
			hs -= h_acc * dt;
		if(hs < 2 && alt <= 2000)
			hs = 0;
		if(vs >= 1) // Added
			vs -= v_acc * dt;
	}

	public void fuelControl(double dw) {
		if (fuel > 0) {
			fuel -= dw;
			weight = (Beresheet_Spacecraft.WEIGHT_EMP + fuel);
			acc = (NN * Actions.accMax(weight));
		} else { // ran out of fuel
			acc = 0;
		}
	}

	public void NNControl() {
		// over 2 km above the ground
		if (alt > 2000) { // maintain a vertical speed of [20-25] m/s
			if (vs > 25) {
				NN = (NN + 0.003 * dt);// more power for braking
			}
			if (vs < 20) {
				NN = (NN - 0.003 * dt);// less power for braking
			}
		} else {
			if (ang > 3) {
				ang -= 3;
			} // rotate to vertical position.
			else {
				ang = 0;
			}

			// Constraint the value 
			NN = pid.control(dt , 0.5 - NN);
			System.out.println("NN : " + NN);
			
			if (alt < 125) { // very close to the ground!
				NN = 1; // maximum braking!
				if (vs < 5) {
					NN = 0.7;
				} // if it is slow enough - go easy on the brakes
			}
			
		}
		if (alt < 5) { // no need to stop
			NN = 0.38;
		}

	}

	public void print() {
		DecimalFormat dfff = new DecimalFormat("#.##");
		String output = "NN : " + dfff.format(NN) + " Time: " + dfff.format(time) + " , VS: " + dfff.format(vs)
				+ " , HS: " + dfff.format(hs) + " , Dist: " + dfff.format(dist) + " ,Lat: " + dfff.format(lat)
				+ " , Alt: " + dfff.format(alt) + " , Ang: " + dfff.format(ang) + " , Weight: " + dfff.format(weight)
				+ " , Acc: " + dfff.format(acc) + " , Fuel : " + dfff.format(fuel);
		System.out.println(output);
	}
}
