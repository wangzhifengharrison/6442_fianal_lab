package pers.James.lab3.lab3_3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/*
 * ColourDial - a colour nob for controlling a value that ranges between 0 and 1.
 * The dials value is changed as you drag the mouse horizontally.
 * Eric McCreath
 */


public class ColourDial extends DialDecorator{

	public ColourDial(Color color, Dial d) {
		super(color,d);
		value = 0.5;
		dragStart = null;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	public double value() {

		return value;
	}

	void drawbackground(Graphics2D g) {  // this paints the background parts of the nob
		super.d.drawbackground(g);
		g.setColor((Color) super.b);
	}


}
