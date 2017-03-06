/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.hex;

import java.util.ArrayList;

/**
 *
 * @author pkale
 */
public class Hex
{

    /**
     *
     * @param q
     * @param r
     * @param s
     */
    public Hex(int q, int r, int s)
    {
        this.q = q;
        this.r = r;
        this.s = s;
    }

    /**
     *
     */
    public final int q;

    /**
     *
     */
    public final int r;

    /**
     *
     */
    public final int s;

    /**
     *
     * @param a
     * @param b
     * @return
     */
    static public Hex add(Hex a, Hex b)
    {
        return new Hex(a.q + b.q, a.r + b.r, a.s + b.s);
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    static public Hex subtract(Hex a, Hex b)
    {
        return new Hex(a.q - b.q, a.r - b.r, a.s - b.s);
    }

    /**
     *
     * @param a
     * @param k
     * @return
     */
    static public Hex scale(Hex a, int k)
    {
        return new Hex(a.q * k, a.r * k, a.s * k);
    }

    /**
     *
     */
    static public ArrayList<Hex> directions = new ArrayList<Hex>(){{add(new Hex(1, 0, -1)); add(new Hex(1, -1, 0)); add(new Hex(0, -1, 1)); add(new Hex(-1, 0, 1)); add(new Hex(-1, 1, 0)); add(new Hex(0, 1, -1));}};

    /**
     *
     * @param direction
     * @return
     */
    static public Hex direction(int direction)
    {
        return Hex.directions.get(direction);
    }

    /**
     *
     * @param hex
     * @param direction
     * @return
     */
    static public Hex neighbor(Hex hex, int direction)
    {
        return Hex.add(hex, Hex.direction(direction));
    }

    /**
     *
     */
    static public ArrayList<Hex> diagonals = new ArrayList<Hex>(){{add(new Hex(2, -1, -1)); add(new Hex(1, -2, 1)); add(new Hex(-1, -1, 2)); add(new Hex(-2, 1, 1)); add(new Hex(-1, 2, -1)); add(new Hex(1, 1, -2));}};

    /**
     *
     * @param hex
     * @param direction
     * @return
     */
    static public Hex diagonalNeighbor(Hex hex, int direction)
    {
        return Hex.add(hex, Hex.diagonals.get(direction));
    }

    /**
     *
     * @param hex
     * @return
     */
    static public int length(Hex hex)
    {
        return (int)((Math.abs(hex.q) + Math.abs(hex.r) + Math.abs(hex.s)) / 2);
    }

    /**
     *
     * @param a
     * @param b
     * @return
     */
    static public int distance(Hex a, Hex b)
    {
        return Hex.length(Hex.subtract(a, b));
    }

}

