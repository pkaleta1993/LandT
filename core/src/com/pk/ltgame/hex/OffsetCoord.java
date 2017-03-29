package com.pk.ltgame.hex;



/**
 * Klasa odpowiadająca za tworzenie obiektów z koordynatami i operacjami na nich.
 * @author pkale
 */
public class OffsetCoord
{

    /**
     *
     * @param col Kolumna.
     * @param row Wiersz.
     */
    public OffsetCoord(int col, int row)
    {
        this.col = col;
        this.row = row;
    }

    /**
     * Kolumna obiektu.
     */
    public final int col;

    /**
     * Wiersz obiektu.
     */
    public final int row;

    /**
     * Wartość dla Hexa parzystego.
     */
    static public int EVEN = 1;

    /**
     * Wartość dla Hexa nieparzystego.
     */
    static public int ODD = -1;

    /**
     *
     * @param offset Wartość hexa(parzysty lub nieparzysty).
     * @param h Hex bazowy.
     * @return OffsetCoordy dla Hexa bazowego.
     */
    static public OffsetCoord qoffsetFromCube(int offset, Hex h)
    {
        int col = h.q;
        int row = h.r + (int)((h.q + offset * (h.q & 1)) / 2);
        return new OffsetCoord(col, row);
    }

    /**
     * Funkcja dla układu [odd/even]-q
     * @param offset Wartość OffsetCoordów(parzyste lub nieparzyste).
     * @param h Bazowe OffsetCoordy
     * @return Hex dla OffsetCoordów.
     */
    static public Hex qoffsetToCube(int offset, OffsetCoord h)
    {
        int q = h.col;
        int r = h.row - (int)((h.col + offset * (h.col & 1)) / 2);
        int s = -q - r;
        return new Hex(q, r, s);
    }

   

    /**
     * Funkcja dla układu [odd/even]-r
     * @param offset Wartość OffsetCoordów(parzyste lub nieparzyste).
     * @param h Bazowe OffsetCoordy
     * @return Hex dla OffsetCoordów.
     */
    static public Hex roffsetToCube(int offset, OffsetCoord h)
    {
        int q = h.col - (int)((h.row + offset * (h.row & 1)) / 2);
        int r = h.row;
        int s = -q - r;
        return new Hex(q, r, s);
    }

}

