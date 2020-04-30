import java.awt.EventQueue;
import javax.swing.JFrame;
/**
 * This class represents the basic flight controller of the Bereshit space
 * craft.
 * 
 * @author ben-moshe
 **/
public class Simulator {
	// 14095, 955.5, 24.8, 2.0
	
	public static void main(String[] args) {
				
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					SpaceCraft spaceCraft = new SpaceCraft();
					
					JFrame frame = new Frame(spaceCraft);
					 
					CPU painterCPU = new CPU(200,"painter"); // 60 FPS painter
					painterCPU.addFunction(frame::repaint);
					painterCPU.play();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
}