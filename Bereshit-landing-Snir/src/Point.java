
import java.text.DecimalFormat;

/*
 * The simple point class
 */

public class Point {
	public double x;
	public double y;
	
	public Point(double x,double y) {
		this.x = x;
		this.y = y;
	}
	
	public Point(Point p) {
		this.x = p.x;
		this.y = p.y;
	}
	
	public Point() {
		x = 0;
		y = 0;
	}
	
	public double distance2D(Point p2) {//new
        double dx = this.x - p2.x;
        double dy = this.y - p2.y;
        double t = (dx*dx+dy*dy);
        return Math.sqrt(t);
	}
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.###");
		
		return "(" + df.format(x) + "," + df.format(y) + ")";
	}

}
