import java.text.DecimalFormat;

public class Algorithm {

	public static double vs;
	public static double hs;
	public static double dist;
	public static double ang;
	public static double alt;
	public static double time;
	public static double dt;
	public static double acc;
	public static double fuel;
	public static double weight;
	public static double NN;

	public static void init() {
		vs = 24.8;
		hs = 932;
		dist = 181 * 1000;
		ang = 58.3; // zero is vertical (as in landing)
		alt = 13748; // 2:25:40 (as in the simulation) // https://www.youtube.com/watch?v=JJ0VfRL9AMs
		time = 0;
		dt = 1; // sec
		acc = 0; // Acceleration rate (m/s^2)
		fuel = 121; //
		weight = Config.WEIGHT_EMP + fuel;
		NN = 0.7; // rate[0,1]
	}

	public static void execute(int deltaTime) {
		DecimalFormat dfff = new DecimalFormat("#.##");
		if (alt > 0) {
			if (time % 10 == 0 || alt < 100) {
				System.out.println("time : " + dfff.format(time) + ", vs : " + dfff.format(vs) + ", hs : "
						+ dfff.format(hs) + ", distance : " + dfff.format(dist) + ", alt : " + dfff.format(alt)
						+ ", ang : " + dfff.format(ang) + ", weight : " + dfff.format(weight) + ", acc : "
						+ dfff.format(acc));
			}
			// over 2 km above the ground
			if (alt > 2000) { // maintain a vertical speed of [20-25] m/s
				if (vs > 25) {
					NN += 0.003 * dt;
				} // more power for braking
				if (vs < 20) {
					NN -= 0.003 * dt;
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
			time += dt;
			double dw = dt * Config.ALL_BURN * NN;
			if (fuel > 0) {
				fuel -= dw;
				weight = Config.WEIGHT_EMP + fuel;
				acc = NN * Utils.accMax(weight);
			} else { // ran out of fuel
				acc = 0;
			}

			v_acc -= vacc;
			if (hs > 0) {
				hs -= h_acc * dt;
			}
			dist -= hs * dt;
			vs -= v_acc * dt;
			alt -= dt * vs;
		}
	}
}
