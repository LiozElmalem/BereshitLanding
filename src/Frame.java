import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Frame extends JFrame{

	private static final long serialVersionUID = 1L;
	private JLabel info;
	private SpaceCraft spaceCraft;
	
	
	Frame(SpaceCraft spaceCraft){
		super("SpaceCraft Landing");

		this.spaceCraft = spaceCraft;
		
		setSize(1800,700);		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton stopBtn = new JButton(" Stop ");
		stopBtn.setVerticalTextPosition(AbstractButton.CENTER);
		stopBtn.setHorizontalTextPosition(AbstractButton.LEADING);
		stopBtn.addActionListener(new ActionListener()
		 
		{
			  public void actionPerformed(ActionEvent e)
			  {
				  spaceCraft.getCPU().stop(); 
			  }
		});
		
		stopBtn.setBounds(0, 10, 150, 50);
		this.getContentPane().add(stopBtn);
		
		JButton startButton = new JButton(" Start ");
		startButton.setVerticalTextPosition(AbstractButton.CENTER);
		startButton.setHorizontalTextPosition(AbstractButton.LEADING);
		startButton.addActionListener(new ActionListener()
		
		{ 
			  public void actionPerformed(ActionEvent e)
			  {
				  	Painter.isStart=true; 
					spaceCraft.land();
			  }
		});
		
		startButton.setBounds(0, 60, 150, 50);
		this.getContentPane().add(startButton);
		
		Painter painter = new Painter(spaceCraft);
		painter.setBounds(0, 0, 1800, 700);
		add(painter);
		
		info = new JLabel("Info");
		info.setBounds(100, 440, 1800, 200);
        info.setFont(new Font("Comic Sans MS", Font.PLAIN, 35));
        info.setForeground(Color.WHITE);
		add(info);
		 
		JLabel background = new JLabel();
		background.setBackground(Color.BLACK);
		background.setOpaque(true);
		add(background);
		
		CPU infoCpu = new CPU(200 , "Info");
		infoCpu.addFunction(this::infoUpdate);
		infoCpu.play();
		
		setVisible(true);
	}
	
	public void infoUpdate(int deltaTime) {
		this.spaceCraft.updateInfo(deltaTime, this.info);
	}
	

	
}
