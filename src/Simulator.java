import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
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
					System.out.println("Simulating Bereshit's Landing:");
					SpaceCraft spaceCraft = new SpaceCraft();
					JFrame frame = new JFrame();
					frame.setSize(1800,700);
					frame.setTitle("SpaceCraft Landing");
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.getContentPane().setLayout(null);
					JButton stopBtn = new JButton(" Stop ");
					stopBtn.addActionListener(new ActionListener()
					
					{
						  public void actionPerformed(ActionEvent e)
						  {
							  spaceCraft.getCPU().stop();
						  }
					});
					
					stopBtn.setBounds(1400, 50, 110, 30);
					frame.getContentPane().add(stopBtn);
					
					JButton startButton = new JButton(" Start ");
					startButton.addActionListener(new ActionListener()
					
					{
						  public void actionPerformed(ActionEvent e)
						  {
								spaceCraft.land();
						  }
					});
					
					startButton.setBounds(1400, 100, 110, 30);
					frame.getContentPane().add(startButton);
					
					Painter painter = new Painter();
					painter.setBounds(0, 0, 2000, 2000);
					frame.getContentPane().add(painter);
					
					frame.setVisible(true);
					
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