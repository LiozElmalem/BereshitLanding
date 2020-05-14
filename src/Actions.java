public class Actions{
		
	public static double accMax(double weight) {
		return acc(weight, true,8);
	}
	public static double acc(double weight, boolean main, int seconds) {
		double t = 0;
		if(main) {t += Beresheet_Spacecraft.MAIN_ENG_F;}
		t += seconds*Beresheet_Spacecraft.SECOND_ENG_F;
		double ans = t/weight;
		return ans;
	}



}
