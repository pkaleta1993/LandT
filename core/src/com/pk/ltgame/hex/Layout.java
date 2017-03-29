package com.pk.ltgame.hex;

import java.util.ArrayList;

/**
 * Klasa odpowiadająca za układ pól
 * @author pkale
 */
public class Layout
{

    /**
     *
     * @param orient Orientacja układu (pointly, flat).
     * @param hSize Rozmiar pola(połowa szerokości).
     * @param oSize Rozmiar pola(Połowa szerokości, wysokość).
     */
    public Layout(Orientation orient, Point hSize, Point oSize)
    {
        this.orient = orient;
        this.hSize = hSize;
        this.oSize = oSize;
    }

    /**
     * Orientacja
     */
    public final Orientation orient;

    /**
     * Rozmiar pola(połowa szerokości).
     */
    public final Point hSize;

    /**
     * Rozmiar pola(Połowa szerokości, wysokość).
     */
    public final Point oSize;

    /**
     * Orientacja dla pointly-top
     */
    static public Orientation pointy = new Orientation(Math.sqrt(3.0), Math.sqrt(3.0) / 2.0, 0.0, 3.0 / 2.0, Math.sqrt(3.0) / 3.0, -1.0 / 3.0, 0.0, 2.0 / 3.0, 0.5);

    /**
     * Orientacja dla flat-top
     */
    static public Orientation flat = new Orientation(3.0 / 2.0, 0.0, Math.sqrt(3.0) / 2.0, Math.sqrt(3.0), 2.0 / 3.0, 0.0, -1.0 / 3.0, Math.sqrt(3.0) / 3.0, 0.0);

    /**
     *
     * @param layout Wykorzystywany Layout.
     * @param h  Wybrany Hex.
     * @return Punkt dla wybranego obiektu Hex. 
     */
    static public Point hexToPix(Layout layout, Hex h)
    {
       
        Orientation M = layout.orient;
        Point size = layout.hSize;
        Point origin = layout.oSize;
        double x = (M.matrixElement0 * h.q + M.matrixElement1 * h.r) * size.x;
        double y = (M.matrixElement2 * h.q + M.matrixElement3 * h.r) * size.y;
        return new Point(x + origin.x, y + origin.y);
    }

    /**
     *
     * @param layout Wykorzystywany Layout.
     * @param p Wybrany punkt.
     * @return FractionalHex dla wybranego punktu.
     */
    static public FractionalHex pixelToHex(Layout layout, Point p)
    {
        Orientation M = layout.orient;
        Point size = layout.hSize;
        Point origin = layout.oSize;
        Point pt = new Point((p.x - origin.x) / size.x, (p.y - origin.y) / size.y);
        double q = M.invertedMatrixElement0 * pt.x + M.invertedMatrixElement1 * pt.y;
        double r = M.invertedMatrixElement2 * pt.x + M.invertedMatrixElement3 * pt.y;
        return new FractionalHex(q, r, -q - r);
    }

    

}

