
public class Engine {

	private boolean alive;
	private double power;
	
	Engine(double power){
		this.alive = false;
		this.power = power;
	}

	public boolean isAlive() {
		return alive;
	}

	public void oposAlive() {
		this.alive = !this.alive;
	}

	public double getPower() {
		return power;
	}

	public void powerUp() {
		this.power += Config.POWER_UP_UNIT;
	}
	
	public void powerDown() {
		this.power -= Config.POWER_UP_UNIT;
	}
	
	public String status() {
		return "[Engine alive : " + this.alive + " , Current power : " + this.power + "]";
	}
	
}
