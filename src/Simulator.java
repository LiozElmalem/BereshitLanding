import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.*;

public class Simulator extends JFrame {

	Beresheet_Spacecraft bs;
	private static final long serialVersionUID = 1L;
	final Image BackroundPath = new ImageIcon(
			"C:\\Users\\user\\Desktop\\Lioz\\Autonomic Robots\\Bereshit-landing-Snir\\images\\backround1.png")
					.getImage();
	final Image SpacecraftPath = new ImageIcon(
			"C:\\Users\\user\\Desktop\\Lioz\\Autonomic Robots\\Bereshit-landing-Snir\\images\\spacecraft.png")
					.getImage();
	
	int NormalizePixel_Y = 40;
	int NormalizePixel_X = 85;
	
	static double lastALT;
	static double lastHS;
	static double lastVS;
	static int x;
	static int y;
	
	public Simulator() {
		bs = new Beresheet_Spacecraft();
		x = (int) bs.getLocation().x;
		y = (int) bs.getLocation().y;

		setSize(1200, 750);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		loop();
	}

	public void paint(Graphics g) {
		g.drawImage(BackroundPath, 0, 0, 1200, 750, null);
		g.setColor(Color.green);
		g.fillOval(Moon.LANDING_AREA_X, Moon.LANDING_AREA_Y, 40, 15);// Landing area
		g.drawImage(SpacecraftPath, x, y, 20, 20, null);
	}

	public void loop() {

		lastALT = bs.getAlt();// 13748 snir
		lastHS = bs.getHS();// 24.8

		while (bs.getAlt() > Moon.realDestinationPoint.y && bs.getLat() < Moon.realDestinationPoint.x) {

			repaint();

			if (bs.getTime() % 10 == 0 || bs.getAlt() < 100) { 
				bs.print();
			}

			bs.NNControl();

			// main computations
			double ang_rad = Math.toRadians(bs.getAng());
			double h_acc = Math.sin(ang_rad) * bs.getAcc();
			double v_acc = Math.cos(ang_rad) * bs.getAcc();
			double vacc = Moon.getAcc(bs.getHS());

			bs.timer();

			double dw = bs.getDT() * Beresheet_Spacecraft.ALL_BURN * bs.getNN(); // Difference weight

			bs.fuelControl(dw);

			v_acc -= vacc;

			bs.speedControl(h_acc, v_acc);
			bs.loactionUpdate();

			if ((lastALT - bs.getAlt() > 1000) && (lastHS - bs.getHS() > 60)) {
				
				y = (int) (bs.getLocation().y + NormalizePixel_Y);
				x = (int) (bs.getLocation().x + NormalizePixel_X);
				
				bs.setLocation(x, y);
				
				lastHS = bs.getHS();
				lastALT = bs.getAlt();
			}

			else if (bs.getAlt() < 1000) { // last km
				y = (int) (bs.getLocation().y + 1);
				bs.setLocation(bs.getLocation().x, y);
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		new Simulator();
	}

}
