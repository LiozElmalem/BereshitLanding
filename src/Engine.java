
public class Engine {

	private String side;
	private double power;
	
	Engine(String side , double power){
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
