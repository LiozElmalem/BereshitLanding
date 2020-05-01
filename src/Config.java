import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public class Config {
	 
	public static final int spaceCraftDimension = 20;
	public static final int targetDimension = 10;
	public static final Point moonPoint = new Point(900 , 350);
	public static final Shape moonCircleBounds = new Ellipse2D.Float(900, 350, 100, 100); // Moon bounds
	public static final Point target = new Point(1000,420); // The red point on the moon
	public static final Point startPoint = new Point(990,0); // The red point on the moon
	
	public static final int START_ALTITUDE = 13748; 
	public static final int NORM_INIT_DISTANCE_FROM_MOON = (int) Utils.getDistanceBetweenPoints(startPoint, moonPoint);
	public static final int NORM_ALT = START_ALTITUDE / NORM_INIT_DISTANCE_FROM_MOON;
	
	public static final int START_DISTANCE = 181 * 1000;
	public static final int START_DISTANCE_NORM = (int) Utils.getDistanceBetweenPoints(startPoint, target);
	public static final int NORM_DISTANCE = START_DISTANCE / START_DISTANCE_NORM;
	
	public static final int ENGINE_AMOUNT = 8;  
	public static final double POWER_UP_UNIT = 8; 
	public static final int MINIMUM_ENGINE_POWER = 10;
	
	/*
	 *  MOON CONFIG
	 */
	public static final double RADIUS = 3475 * 1000; // meters
	public static final double RADIUS_NORM = 75;
	public static final double NORM_RADIUS = RADIUS / RADIUS_NORM;
	
	public static final double ACC = 1.622;// m/s^2
	public static final double EQ_SPEED = 1700;// meter/second
	// Weight properties
	public static final double WEIGHT_EMP = 165; // kilogram
	public static final double WEIGHT_FULE = 420; // kilogram
	public static final double WEIGHT_FULL = WEIGHT_EMP + WEIGHT_FULE; // kilogram
	// https://davidson.weizmann.ac.il/online/askexpert/%D7%90%D7%99%D7%9A-%D7%9E%D7%98%D7%99%D7%A1%D7%99%D7%9D-%D7%97%D7%9C%D7%9C%D7%99%D7%AA-%D7%9C%D7%99%D7%A8%D7%97
	public static final double MAIN_ENG_F = 430; // N
	public static final double SECOND_ENG_F = 25; // N
	// Fuel Properties
	public static final double MAIN_BURN = 0.15; //liter per second, 12 liter per m'
	public static final double SECOND_BURN = 0.009; //liter per second 0.6 liter per m'
	public static final double ALL_BURN = MAIN_BURN + 8 * SECOND_BURN;
}
