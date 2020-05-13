import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.*;


public class Simulator extends JFrame{
	Beresheet_Spacecraft bs;
	private static final long serialVersionUID = 1L;	
	private static final Image BackroundPath=new ImageIcon("C:\\Users\\Snir\\git\\Bereshit-landing\\images\\backround1.png").getImage();
	private static final Image SpacecraftPath=new ImageIcon("C:\\Users\\Snir\\git\\Bereshit-landing\\images\\spacecraft.png").getImage();
	private static double Normalize=150;
	private static int NormalizePixel_Y=40;
	private static int NormalizePixel_X=85;

	
	static double lastALT;
	static double lastHS;
	static double lastVS;
	static int x;
	static int y;
	Point landingPoint=new Point(Moon.LANDING_AREA_X,Moon.LANDING_AREA_Y);
	
	
	public Simulator() {
		bs=new Beresheet_Spacecraft();
		x=(int) bs.getPoint().x;
		y=(int) bs.getPoint().y;
		 
		setSize(1200,750);
		setLayout(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);

		loop();		
	}

	public void paint(Graphics g) {
		g.drawImage(BackroundPath, 0, 0, 1200, 750, null);
		g.setColor(Color.green);
		g.fillOval(Moon.LANDING_AREA_X, Moon.LANDING_AREA_Y, 40, 15);//Landing area
		g.drawImage(SpacecraftPath,x,y,20,20,null);
	}
	
	
	public void loop() {
		lastALT=bs.getAlt();//13748 snir
		lastHS=bs.getHS();//24.8
		//lastVS=bs.getVS();//932
		
		while(bs.getAlt()>0) {
			repaint();//snir			 
	
			if(bs.getTime()%10==0 || bs.getAlt()<100) {
				System.out.println(bs.toString());
				
				Normalize-=2.4;//snir
			}
			// over 2 km above the ground
			if(bs.getAlt()>2000) {	// maintain a vertical speed of [20-25] m/s
				if(bs.getVS() >25) {
					bs.setNN(bs.getNN()+0.003*bs.getDT());// more power for braking

				} 
				if(bs.getVS() <20) {
					bs.setNN(bs.getNN()-0.003*bs.getDT());// less power for braking
				} 
			}
			// lower than 2 km - horizontal speed should be close to zero
			else {
				if(bs.getAng()>3) {
					bs.setAng(bs.getAng()-3);
				} // rotate to vertical position.
				else {
					bs.setAng(0);
				}
				
				bs.setNN(0.5); // brake slowly, a proper PID controller here is needed!          PID

				
				if(bs.getHS()<2) {
					bs.setHS(0);
				}
				if(bs.getAlt()<125){ // very close to the ground!
					bs.setNN(1); // maximum braking!
					if(bs.getVS()<5) {
						bs.setNN(0.7);	
					} // if it is slow enough - go easy on the brakes 
				}
			}
			if(bs.getAlt()<5) { // no need to stop
				bs.setNN(0.4);
				
			}
			
			bs.updateAllEnginesPower(bs.getNN());//Update the engines power                 snir

			
			// main computations
			double ang_rad = Math.toRadians(bs.getAng());
			double h_acc = Math.sin(ang_rad)*bs.getAcc();
			double v_acc = Math.cos(ang_rad)*bs.getAcc();
			double vacc = Moon.getAcc(bs.getHS());	
			bs.setTime(bs.getTime()+bs.getDT());
			double dw = bs.getDT()*Beresheet_Spacecraft.ALL_BURN*bs.getNN();
			if(bs.getFuel()>0) {
				bs.setFuel(bs.getFuel()- dw);
				bs.setWeight(Beresheet_Spacecraft.WEIGHT_EMP + bs.getFuel());
				bs.setAcc(bs.getNN()* Actions.accMax(bs.getWeight()));
			}
			else { // ran out of fuel
				bs.setAcc(0);
			}

			v_acc -= vacc; 
			if(bs.getHS()>0) {bs.setHS(bs.getHS()- h_acc*bs.getDT());}
			
			bs.setVS(bs.getVS()- v_acc*bs.getDT());
			bs.setAlt(bs.getAlt()-bs.getDT()*bs.getVS()); 
			
			
			//////////////////////////////////////////////////////////////////////////////////snir
			if(bs.getAlt()>1000) {
				//bs.setDist(bs.getDist()-bs.getHS()*bs.getDT());//boaz
				bs.setDist(bs.getPoint().distance2D(landingPoint)*Normalize);
			}else {
				bs.setDist(bs.getAlt());
			}
			
			if((lastALT-bs.getAlt()>1000) && (lastHS-bs.getHS()>60)) {
				y=(int)(bs.getPoint().y+NormalizePixel_Y);
				x=(int)(bs.getPoint().x+NormalizePixel_X);
				bs.p.y=y;
				bs.p.x=x;
				lastHS=bs.getHS();
				lastALT=bs.getAlt();
			}
		
			else if(bs.getAlt()<1000) {//last km
				y=(int)(bs.getPoint().y+1);
				bs.p.y=y;
			}
			//////////////////////////////////////////////////////////////////////////////////snir

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



