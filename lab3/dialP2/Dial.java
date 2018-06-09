package pers.James.lab3.lab3_3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;

public abstract class Dial extends JComponent implements MouseMotionListener, MouseListener {

    final static double MOUSEVALUESCALINGFACTOR = 100.0;
    final static Dimension dim = new Dimension(70,70);
    final static int inset = 8;
    final static double dialRadius = dim.width/2.0-inset;
    final static double tickOuterRadius = dim.width / 2.0 - inset / 4.0;
    final static double markerInnerRadius = dim.width / 4.0;

    double value; // range from 0.0 - 1.0

    Integer dragStart;

    DialGUI dialgui;

    void drawbackground(Graphics2D g) {  // this paints the background parts of the nob
        g.setColor(Color.white);
        g.fillRect(0, 0, dim.width, dim.height);

        g.setColor(Color.black);
        g.setStroke(new BasicStroke(1.0f));
        drawtick(g, 0.0, dialRadius,  tickOuterRadius);
        drawtick(g, 1.0, dialRadius,  tickOuterRadius);

        g.setColor(Color.LIGHT_GRAY);
    }

    abstract double value();

    public Dimension getPreferredSize() {
        return dim;
    }

    @Override
    public
    void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        drawbackground(g);
        drawdial(g,value());
    }
    // draw a line that radiates from the center of the dial
    // the angle of the line is based on the value for this dial
    public void drawtick(Graphics2D g, double v, double s, double e) {
        double ang = (1.0 - v) * Math.PI * 2.0 * 0.8 + Math.PI * 0.2;
        double x1 = Math.sin(ang)*s + dim.width/2.0;
        double y1 = Math.cos(ang)*s + dim.height/2.0;
        double x2 = Math.sin(ang)*e + dim.width/2.0;
        double y2 = Math.cos(ang)*e + dim.height/2.0;
        g.draw(new Line2D.Double(x1,y1,x2,y2));
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        if (dragStart != null) {
            value += (me.getX() - dragStart)/MOUSEVALUESCALINGFACTOR;
            if (value < 0.0) value = 0.0;
            if (value > 1.0) value = 1.0;
            dragStart = me.getX();
        }
        dialgui.update();
        this.repaint();
    }

    void drawdial(Graphics2D g, double v) { // this paints the foreground parts of the nob
        g.fill(new Arc2D.Double(inset, inset, dialRadius*2.0, dialRadius*2.0, 0, 360, Arc2D.OPEN));
        g.setColor(Color.black);
        g.setStroke(new BasicStroke(4.0f));
        drawtick(g, v, dialRadius, markerInnerRadius);
    }

    @Override
    public void mouseMoved(MouseEvent me) {
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        dragStart = me.getX();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        dragStart = null;
    }


    public void setGUI(DialGUI dialGUI) {
        dialgui = dialGUI;
    }



}
