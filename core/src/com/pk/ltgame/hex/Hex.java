package com.pk.ltgame.hex;

import java.util.ArrayList;

/**
 * Klasa odpowiadająca za operację na polach typu Hex.
 * @author pkale
 */
public class Hex
{

    /**
     *
     * @param q Parametr q.
     * @param r Parametr r.
     * @param s Parametr s.
     */
    public Hex(int q, int r, int s)
    {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     * Wartość q obiektu.
     */
    public final int q;

    /**
     * Wartość r obiektu.
     */
    public final int r;

    /**
     * Wartość s obiektu.
     */
    public final int s;

    /**
     *
     * @param a - Obiekt Hex.
     * @param b - Obiekt Hex.
     * @return Nowy Hex o dodanych do a koordynatach b.
     */
    static public Hex add(Hex a, Hex b)
    {
        return new Hex(a.q + b.q, a.r + b.r, a.s + b.s);
    }

    /**
     *
     * @param a - Obiekt Hex.
     * @param b - Obiekt Hex.
     * @return Nowy Hex o odjętych od a koordynatach b.
     */
    static public Hex sub(Hex a, Hex b)
    {
        return new Hex(a.q - b.q, a.r - b.r, a.s - b.s);
    }

    

    /**
     * Lista kierunków(Każdy obiekt stanowi wartości, które dodane do danego obiektu Hex, dadzą Hexy sąsiednie).
     */
    static public ArrayList<Hex> dir = new ArrayList<Hex>(){{add(new Hex(1, 0, -1)); add(new Hex(1, -1, 0)); add(new Hex(0, -1, 1)); add(new Hex(-1, 0, 1)); add(new Hex(-1, 1, 0)); add(new Hex(0, 1, -1));}};

    /**
     *
     * @param dir Id obiektu na liście kierunków - od 0 do 5.
     * @return Koordynaty określonego kierunku.
     */
    static public Hex getDir(int dir)
    {
        return Hex.dir.get(dir);
    }

    /**
     *
     * @param hex Hex bazowy.
     * @param dir Koordynaty kierunku.
     * @return Sąsiedni hex o określonym do bazy kierunku.
     */
    static public Hex neighbor(Hex hex, int dir)
    {
        return Hex.add(hex, Hex.getDir(dir));
    }

    
    /**
     *
     * @param hex Obiekt Hex.
     * @return Suma wartości abolutnych koordynatów(|[variable]|).
     */
    static public int len(Hex hex)
    {
        return (int)((Math.abs(hex.q) + Math.abs(hex.r) + Math.abs(hex.s)) / 2);
    }

    /**
     *
     * @param a Pierwszy punkt drogi.
     * @param b Drugi punkt drogi.
     * @return Odległość między punktami.
     */
    static public int dis(Hex a, Hex b)
    {
        return Hex.len(Hex.sub(a, b));
    }

}

