/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.hex;



/**
 *
 * @author pkale
 */
public class OffsetCoord
{

    /**
     *
     * @param col
     * @param row
     */
    public OffsetCoord(int col, int row)
    {
        this.col = col;
        this.row = row;
    }

    /**
     *
     */
    public final int col;

    /**
     *
     */
    public final int row;

    /**
     *
     */
    static public int EVEN = 1;

    /**
     *
     */
    static public int ODD = -1;

    /**
     *
     * @param offset
     * @param h
     * @return
     */
    static public OffsetCoord qoffsetFromCube(int offset, Hex h)
    {
        int col = h.q;
        int row = h.r + (int)((h.q + offset * (h.q & 1)) / 2);
        return new OffsetCoord(col, row);
    }

    /**
     *
     * @param offset
     * @param h
     * @return
     */
    static public Hex qoffsetToCube(int offset, OffsetCoord h)
    {
        int q = h.col;
        int r = h.row - (int)((h.col + offset * (h.col & 1)) / 2);
    //  int r = (int)(h.row - ((h.col + offset * (h.col & 1)) / 2));
      // int hba = h.col&1;
      // System.out.println("Offset = " + offset + " oraz h.col&1 = " + hba);
      // int r = h.row - (int)(( h.col + offset * (abs(h.col)%2)) / 2);
        int s = -q - r;
        
        //System.out.println("Q: " + q +" R: " + r + " S: " +s);
        return new Hex(q, r, s);
    }

    /**
     *
     * @param offset
     * @param h
     * @return
     */
    static public OffsetCoord roffsetFromCube(int offset, Hex h)
    {
        int col = h.q + (int)((h.r + offset * (h.r & 1)) / 2);
        int row = h.r;
        return new OffsetCoord(col, row);
    }

    /**
     *
     * @param offset
     * @param h
     * @return
     */
    static public Hex roffsetToCube(int offset, OffsetCoord h)
    {
        int q = h.col - (int)((h.row + offset * (h.row & 1)) / 2);
        int r = h.row;
        int s = -q - r;
        return new Hex(q, r, s);
    }

}

