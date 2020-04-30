import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JComponent;

public class Painter extends JComponent {

	private static final long serialVersionUID = 1L;
	private SpaceCraft spaceCraft;

	Painter(SpaceCraft spaceCraft) {
		this.spaceCraft = spaceCraft;
		repaint();
	}

	public void paintMoon(Graphics g) {
		g.drawImage(
				Toolkit.getDefaultToolkit().getImage(
						"C:\\Users\\user\\Desktop\\Lioz\\Autonomic Robots\\Bereshit-landing-master\\images\\moon.jpeg"),
				900, 350, null);
	}

	public void paintSpaceCraft(Graphics g) {
		g.drawImage(
				Toolkit.getDefaultToolkit().getImage(
						"C:\\Users\\user\\Desktop\\Lioz\\Autonomic Robots\\Bereshit-landing-master\\images\\spaceCraft2.jpeg"),
				(int) this.spaceCraft.getLocation().x / 500, (int) this.spaceCraft.getLocation().y / 500, null);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Color.black);
		paintSpaceCraft(g);
		paintMoon(g);
		this.setVisible(true);
	}
}
