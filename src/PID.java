
public class PID {
	
	private double P, I, D;
	private double lastError, sumIntegral;
    private int[] range;
    
    /*
     * P = present
     * I = past
     * D = future
     */
	public PID(int P , int I , int D , int max){
      this.P = P;
      this.I = I;
      this.D = D;
      this.lastError = 0;
      this.sumIntegral = 0;
      this.range = new int[2];
      this.range[0] = -max;
      this.range[1] = max;
	}
    	
    public double control(double dt , double error) {
    	this.sumIntegral += this.I * error * dt;
    	double difference = (error - lastError) / dt;
    	double constIntegral = (this.sumIntegral >= range[0]) ? this.sumIntegral : 0; 
    	constIntegral = (this.sumIntegral <= range[1]) ? this.sumIntegral : range[1]; 
    	double output = this.P * error + this.D * difference + constIntegral;
    	this.lastError = error;
    	return output;
    }

}
