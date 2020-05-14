
public class NavigationEngine {

	// Maximum power of 25 kg fuel (every small engine)

	private String side;
	private double power;

	NavigationEngine(String side, double power) {
		this.power = power;
		this.side = side;
	}

	public double getPower() {
		return this.power;
	}

	public void setPower(double power) {
		this.power = power;
	}

	public String getSide() {
		return this.side;
	}

}
