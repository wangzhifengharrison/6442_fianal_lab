package pers.James.lab3.lab3_3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DialGUI implements Runnable {

	private static final Dimension COLORDIM = new Dimension(SimpleDial.dim.height *3,SimpleDial.dim.height *3);
	/**
	 * DialGUI - this application enables the users to change
	 *           the color of a rectangle by altering 3 dial. 
	 *           The dial alter the RGB components of the color. 
	 * Eric McCreath 2015 -
	 */

	JFrame jframe;
	JComponent colorComp;
	Dial dialr, dialg, dialb;
	
	public DialGUI() {
		SwingUtilities.invokeLater(this);
	}

	public static void main(String[] args) {
		new DialGUI();
	}

	public void run() {
		jframe = new JFrame("DialGUI");
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		dialr = new ColourDial(Color.red, new ClickDial(10,new SimpleDial()));
		dialg = new ColourDial(Color.green, new ClickDial(10,new SimpleDial()));
		dialb = new ColourDial(Color.blue, new ClickDial(10, new SimpleDial()));
        colorComp = new JPanel() {
        	protected void paintComponent(Graphics g) {
        		g.setColor(this.getForeground());
        		g.fillRect(0, 0, COLORDIM.width, COLORDIM.height);
        	}
        };
        colorComp.setPreferredSize(COLORDIM);
        JPanel dialpanel = new JPanel();
        dialpanel.setLayout(new GridLayout(3,1));
        dialpanel.add(dialr); 
        dialpanel.add(dialg); 
        dialpanel.add(dialb); 
        dialr.setGUI(this);
        dialg.setGUI(this);
        dialb.setGUI(this);
		jframe.getContentPane().add(dialpanel,BorderLayout.EAST);
		jframe.getContentPane().add(colorComp,BorderLayout.CENTER);
		
        update();
		jframe.setVisible(true);
		jframe.pack();
	}
	
	public void update() {
		float r = (float) dialr.value();
		float g = (float) dialg.value();
		float b = (float) dialb.value();
		colorComp.setForeground(new Color(r,g,b));
		colorComp.repaint();
	}	
}
