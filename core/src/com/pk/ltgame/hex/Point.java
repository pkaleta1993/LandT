package com.pk.ltgame.hex;

/**
 * Klasa pomocniczna do trzymania koordynatów.
 * @author pkale
 */
public class Point
{

    /**
     * Wartość x obiektu.
     */
    public final double x;

    /**
     * Wartośc y obiektu.
     */
    public final double y;
    
    /**
     *
     * @param x Położenie X.
     * @param y Położenie Y
     */
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

}
