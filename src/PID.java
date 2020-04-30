
public class PID {
	
	private int P, I, D;
	private double lastError = 0 , sumIntegral = 0;
    private int max;
    
	public PID(int max){
      this.P = 0;
      this.I = 0;
      this.D = 0;
      this.max = max;
    }
    	
    public double execute(double dt , double error) {
    	this.sumIntegral += this.I * error * dt;
    	double difference = (error - lastError) / dt;
    	double constIntegral = (this.sumIntegral >= -this.max) ? this.sumIntegral : 0; 
    	constIntegral = (this.sumIntegral <= this.max) ? this.sumIntegral : this.max; 
    	double output = this.P * error + this.D * difference + constIntegral;
    	this.lastError = error;
    	return output;
    }

}
