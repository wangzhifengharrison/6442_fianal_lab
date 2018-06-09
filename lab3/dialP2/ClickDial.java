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
 * ClickDial - a grey nob for controlling a value that ranges between 0 and 1.
 * The nob has click points, so the dial can only be set to fractional points 
 * within the range.
 * The dials value is changed as you drag the mouse horizontally.
 * Eric McCreath
 */


public class ClickDial extends DialDecorator{

	public ClickDial(int clicks, Dial d) {
		super(clicks, d);
		value = 0.5;
		dragStart = null;
		this.addMouseMotionListener(this);
		this.addMouseListener(this);
	}
	
	public double value() {

		return ((double) Math.round(value * (int)super.b)) / (int)super.b;
	}


	void drawbackground(Graphics2D g) {  // this paints the background parts of the nob
		super.d.drawbackground(g);

		for (int s = 1; s < (int)super.b; s++)
			super.drawtick(g, s / ((int)super.b * 1.0),dialRadius,  tickOuterRadius);
	}


}
