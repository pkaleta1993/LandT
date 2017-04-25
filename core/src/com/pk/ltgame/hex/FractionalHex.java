package com.pk.ltgame.hex;

import java.util.ArrayList;

/**
 * Klasta odpowiedzialna za operację na polach typu FractionalHex
 * @author pkale
 */

public class FractionalHex
{

    /**
     * Konstruktor FractionalHex
     * @param q Parametr q dla Fractional Hex.
     * @param r Parametr r dla Fractional Hex.
     * @param s Parametr s dla Fractional Hex.
     */
    public FractionalHex(double q, double r, double s)
    {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     * Wartość q.
     */
    public final double q;

    /**
     * Wartość r.
     */
    public final double r;

    /**
     * Wartość s.
     */
    public final double s;

    /**
     * Zaokrąglanie koordynatów do Hex
     * @param h FractionalHex do zaokrąglania
     * @return Zaokrąglone koordynaty opakowane w klase Hex
     */
    static public Hex hexRound(FractionalHex h)
    {
        int q = (int)(Math.round(h.q));
        int r = (int)(Math.round(h.r));
        int s = (int)(Math.round(h.s));
        double qDifference = Math.abs(q - h.q);
        double rDifference = Math.abs(r - h.r);
        double sDifference = Math.abs(s - h.s);
        if (qDifference > rDifference && qDifference > sDifference)
        {
            q = -r - s;
        }
        else
            if (rDifference > sDifference)
            {
                r = -q - s;
            }
            else
            {
                s = -q - r;
            }
       
        return new Hex(q, r, s);
    }

   

}
