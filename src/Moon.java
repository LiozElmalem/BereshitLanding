public class Moon {

	public static final double RADIUS = 3475*1000; // meters
	public static final double ACC = 1.622;// m/s^2
	public static final double EQ_SPEED = 1700;// m/s
	
	public static final int LANDING_AREA_X = 1012;
	public static final int LANDING_AREA_Y = 650;
	
	public static double getAcc(double speed) {
		double n = Math.abs(speed)/EQ_SPEED;
		double ans = (1-n)*ACC;
		return ans;
	}
}
