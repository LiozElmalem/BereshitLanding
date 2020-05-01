import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JComponent;

public class Painter extends JComponent {

	private static final long serialVersionUID = 1L;
	private SpaceCraft spaceCraft;
	
	private double radians = 0; //snir &&&&&&&&&&&&&&&&&&&
	private double velocity = 0.003; // snir &&&&&&&&&&&&&&&&&
 
	Painter(SpaceCraft spaceCraft) {
		this.spaceCraft = spaceCraft;
		repaint();
	}
	
	public void paintStars(Graphics g){
		Random random = new Random();
		int xPosition = (int)(random.nextDouble() * 1800);
		int yPosition = (int)(random.nextDouble() * 700);
		int[] X1 = {xPosition, xPosition+5, xPosition+10};
		int[] Y1 = {yPosition + 10, yPosition, yPosition+10};
		int[] X2 = {xPosition, xPosition+10, xPosition+5};
		int[] Y2 = {yPosition +5, yPosition+5, yPosition+10};
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillPolygon(X1, Y1, 3);
		g.fillPolygon(X2, Y2, 3);	
	}
	
	public void paintLaser(Graphics g) {
		g.setColor(Color.RED);
		g.drawLine((int) Math.round(spaceCraft.getLocation().x + Config.spaceCraftDimension), (int)Math.round(spaceCraft.getLocation().y + Config.spaceCraftDimension), (int)Config.target.x + (Config.targetDimension / 2), (int)Config.target.y + (Config.targetDimension));
	}

	public void paintTarget(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval((int)Config.target.x, (int)Config.target.y, Config.targetDimension, Config.targetDimension);
	}
	
	public void paintMoon(Graphics g) {
		g.drawImage(
				Toolkit.getDefaultToolkit().getImage(
						"C:\\Users\\Snir\\git\\Bereshit-landing\\images\\moon.jpeg"),
				(int)Config.moonPoint.x, (int)Config.moonPoint.y, null);
	}

	public void paintSpaceCraft(Graphics g) {
		g.drawImage(
				Toolkit.getDefaultToolkit().getImage(
						"C:\\Users\\Snir\\git\\Bereshit-landing\\images\\spaceCraft2.jpeg"),
				(int) this.spaceCraft.getLocation().x, (int) this.spaceCraft.getLocation().y, null);
	}

	public void paintOrbit(Graphics g) {//snir $$$$$$$$$$$$$$$$$$$$$$$$$$$$
		double[] XandY= {Config.target.x ,Config.target.y};
		g.drawImage(
				Toolkit.getDefaultToolkit().getImage(
						"C:\\Users\\Snir\\git\\Bereshit-landing\\images\\spaceCraft2.jpeg"),
				(int)updateOrbit(XandY)[0], (int)updateOrbit(XandY)[1], null);
	}

	
	private double[] updateOrbit(double [] XandY) {///snir $$$$$$$$$$$$$$$$$$
		this.radians+=this.velocity;
		XandY[0] += Math.cos(this.radians)*400;
		XandY[1] += Math.sin(this.radians)*200;
		
		return XandY;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.black);
		paintMoon(g);
	//	paintSpaceCraft(g);
		paintTarget(g);	
		paintLaser(g);
		paintStars(g);
		paintOrbit(g); //snir &&&&&&&&&&&&&&&&&&&&&&&&&&&&
		setVisible(true);
	}
}
