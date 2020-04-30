
public class Utils {
	// from: https://he.wikipedia.org/wiki/%D7%94%D7%99%D7%A8%D7%97
	public static double getAcc(double speed) {
		double n = Math.abs(speed)/Config.EQ_SPEED;
		double ans = (1-n)*Config.ACC;
		return ans;
	}
	public static double accMax(double weight) {
		return acc(weight, true,8);
	}
	public static double getDistanceBetweenPoints(Point from, Point to) {
		double x1 = (from.x - to.x) * (from.x - to.x);
		double y1 = (from.y - to.y) * (from.y - to.y);
		return Math.sqrt(x1 + y1);
	} 

	public static double acc(double weight, boolean main, int seconds) {
		double t = 0;
		if(main) {t += Config.MAIN_ENG_F;}
		t += seconds*Config.SECOND_ENG_F;
		double ans = t/weight;
		return ans;
	}
}