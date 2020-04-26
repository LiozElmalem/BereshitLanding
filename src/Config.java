
public class Config {
	
	public static final int ENGINE_AMOUNT = 8; 
	public static final double POWER_UP_UNIT = 8; 
	public static final int MINIMUM_ENGINE_POWER = 10;
	
	// MOON CONFIG
	public static final double RADIUS = 3475*1000; // meters
	public static final double ACC = 1.622;// m/s^2
	public static final double EQ_SPEED = 1700;// m/s
	public static final double WEIGHT_EMP = 165; // kg
	public static final double WEIGHT_FULE = 420; // kg
	public static final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FULE; // kg
// https://davidson.weizmann.ac.il/online/askexpert/%D7%90%D7%99%D7%9A-%D7%9E%D7%98%D7%99%D7%A1%D7%99%D7%9D-%D7%97%D7%9C%D7%9C%D7%99%D7%AA-%D7%9C%D7%99%D7%A8%D7%97
	public static final double MAIN_ENG_F = 430; // N
	public static final double SECOND_ENG_F = 25; // N
	public static final double MAIN_BURN = 0.15; //liter per sec, 12 liter per m'
	public static final double SECOND_BURN = 0.009; //liter per sec 0.6 liter per m'
	public static final double ALL_BURN = MAIN_BURN + 8 * SECOND_BURN;
}
