import java.util.ArrayList;

public class SpaceCraft {

	private ArrayList<Engine> engines;
	public SpaceCraftStatus status;
	private Point location;
	private CPU cpu;
	private PID pid;
	
	SpaceCraft(){
		Algorithm.init();
		this.cpu = new CPU(200, "update");
		cpu.addFunction(Algorithm::execute);
		this.engines = new ArrayList<Engine>(Config.ENGINE_AMOUNT);
		this.location = new Point(0,0);
		engines.forEach((engine) -> engine = new Engine(Config.MINIMUM_ENGINE_POWER));
		this.status = SpaceCraftStatus.ON_GROUND;
		this.pid = new PID(1000);
	}
	
	public synchronized void land() {
		this.cpu.play(); 
	}
	
	public PID getPID() {
		return this.pid;
	}
	
	public CPU getCPU() {
		return this.cpu;
	}
	
	public void setLocation(Point location) {
		this.location = location;
	}
	
	public Point getLocation() {
		return this.location;
	}
	
	public ArrayList<Engine> getEngines(){
		return this.engines;
	}
	
	public static enum SpaceCraftStatus {
		ON_GROUND, 
		TAKEOFF , 
		LANDING
	}
}
