import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JComponent;


public class Painter extends JComponent {

	private static final long serialVersionUID = 1L;
	private SpaceCraft spaceCraft;

	private static String str;
	private static int x,y;
	private double RotaryForce = 400; 
	private double radians = 0; 
	private double velocity = 0.001; 
	public static boolean isStart=false; 
	public static boolean isLanding=false; 
	public static boolean isLastKM=false; 

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
		//((Graphics2D) g).rotate(Math.toRadians(-25)/Math.PI, x, y);
		g.drawLine(x, y,(int)Config.landindCordinate.x , (int)Config.landindCordinate.y);
	}


	public void paintMoon(Graphics g) {
		str="C:\\Users\\Snir\\git\\Bereshit-landing\\images\\moon.jpeg";
		drawImageByPath(g,str,(int)Config.moonPoint.x,(int)Config.moonPoint.y);
	}

	public void paintSpaceCraftLanding(Graphics g) {
		str="C:\\Users\\Snir\\git\\Bereshit-landing\\images\\spaceCraft2.jpeg";
		x=(int) this.spaceCraft.getLocation().x;
		y=(int) this.spaceCraft.getLocation().y;
		drawImageByPath(g,str,x,y);
	}

	public void paintOrbit(Graphics g) {
		double[] XandY= {Config.target.x ,Config.target.y};	
		str="C:\\Users\\Snir\\git\\Bereshit-landing\\images\\spaceCraft.jpeg";
		x=(int) updateOrbit(XandY)[0];
		y=(int) updateOrbit(XandY)[1];
		if(isLanding) {
			this.RotaryForce*=0.999;
			this.radians-=0.0001;
//			Config.target.x=Config.landindCordinate.x;
//			Config.target.y=Config.landindCordinate.y;
		}		
		drawImageByPath(g,str,x,y);
	}


	private double[] updateOrbit(double [] XandY) {
		this.radians+=this.velocity;
		XandY[0] += Math.cos(this.radians)*RotaryForce;
		XandY[1] += Math.sin(this.radians)*RotaryForce/2;
		return XandY;
	}

	private void drawImageByPath(Graphics g,String str ,int x,int y){
		g.drawImage(Toolkit.getDefaultToolkit().getImage(str),x, y, null);
	}

	private void findLandingLocation(Graphics g){
		g.setColor(Color.GREEN);
		g.fillRect((int)Config.landindCordinate.x, (int)Config.landindCordinate.y, Config.targetDimension, Config.targetDimension);		
	}
	int i=0;
	private void startLanding(Graphics g) {
		str="C:\\Users\\Snir\\git\\Bereshit-landing\\images\\spaceCraft2.jpeg";
		drawImageByPath(g,str,(int) this.spaceCraft.getLocation().x, (int) this.spaceCraft.getLocation().y);
		if(i<500) {
		spaceCraft.setLocation(new Point(990, ((i++) + 10) ));
		}
	}
	

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(!isLastKM) {
			setBackground(Color.black);
			paintMoon(g);
			paintStars(g);

			if(!isStart) {
				paintOrbit(g); 
			}else {
				paintOrbit(g);
				paintLaser(g);
				findLandingLocation(g);
				isLanding=true;//start bracking the spacecraft
				if(Config.moonCircleBounds.contains(x ,y)) {
					isLastKM=true;
				}
			}
		}else {
//			super.paintComponent(g);
			setBackground(Color.black);
			g.setColor(Color.gray);
			g.fillRect(0, 600, 3000, 3000);
			startLanding(g);
		}
		setVisible(true);
	}
}
