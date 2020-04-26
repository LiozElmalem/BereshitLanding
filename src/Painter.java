import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JComponent;

public class Painter extends JComponent {
	
	private static final long serialVersionUID = 1L;

	Painter(){
	}
	
	@Override
	public void paintComponent(Graphics g) {
		   super.paintComponent(g);
		   Image img = Toolkit.getDefaultToolkit().getImage("C:\\Users\\user\\Desktop\\Lioz\\Autonomic Robots\\Matala2\\images\\moon.jpeg");
		   g.drawImage(img, 0, 0, null);
           setVisible(true);
	}
}
