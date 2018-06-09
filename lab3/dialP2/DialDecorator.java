package pers.James.lab3.lab3_3;

import java.awt.event.MouseEvent;

public abstract class DialDecorator extends Dial {
    Dial d;
    Object b;
    abstract double value();
    public DialDecorator(Object b, Dial d){
        this.d = d;
        this.b = b;
    }
}
