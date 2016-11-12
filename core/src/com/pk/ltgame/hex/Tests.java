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
public class Tests
{
    public Tests()
    {
    }

    static public void equalHex(String name, Hex a, Hex b)
    {
        if (!(a.q == b.q && a.s == b.s && a.r == b.r))
        {
            Tests.complain(name);
        }
    }


    static public void equalOffsetcoord(String name, OffsetCoord a, OffsetCoord b)
    {
        if (!(a.col == b.col && a.row == b.row))
        {
            Tests.complain(name);
        }
    }


    static public void equalInt(String name, int a, int b)
    {
        if (!(a == b))
        {
            Tests.complain(name);
        }
    }


    static public void equalHexArray(String name, ArrayList<Hex> a, ArrayList<Hex> b)
    {
        Tests.equalInt(name, a.size(), b.size());
        for (int i = 0; i < a.size(); i++)
        {
            Tests.equalHex(name, a.get(i), b.get(i));
        }
    }


    static public void testHexArithmetic()
    {
        Tests.equalHex("hex_add", new Hex(4, -10, 6), Hex.add(new Hex(1, -3, 2), new Hex(3, -7, 4)));
        Tests.equalHex("hex_subtract", new Hex(-2, 4, -2), Hex.subtract(new Hex(1, -3, 2), new Hex(3, -7, 4)));
    }


    static public void testHexDirection()
    {
        Tests.equalHex("hex_direction", new Hex(0, -1, 1), Hex.direction(2));
    }


    static public void testHexNeighbor()
    {
        Tests.equalHex("hex_neighbor", new Hex(1, -3, 2), Hex.neighbor(new Hex(1, -2, 1), 2));
    }


    static public void testHexDiagonal()
    {
        Tests.equalHex("hex_diagonal", new Hex(-1, -1, 2), Hex.diagonalNeighbor(new Hex(1, -2, 1), 3));
    }


    static public void testHexDistance()
    {
        Tests.equalInt("hex_distance", 7, Hex.distance(new Hex(3, -7, 4), new Hex(0, 0, 0)));
    }


    static public void testHexRound()
    {
        FractionalHex a = new FractionalHex(0, 0, 0);
        FractionalHex b = new FractionalHex(1, -1, 0);
        FractionalHex c = new FractionalHex(0, -1, 1);
        Tests.equalHex("hex_round 1", new Hex(5, -10, 5), FractionalHex.hexRound(FractionalHex.hexLerp(new FractionalHex(0, 0, 0), new FractionalHex(10, -20, 10), 0.5)));
        Tests.equalHex("hex_round 2", FractionalHex.hexRound(a), FractionalHex.hexRound(FractionalHex.hexLerp(a, b, 0.499)));
        Tests.equalHex("hex_round 3", FractionalHex.hexRound(b), FractionalHex.hexRound(FractionalHex.hexLerp(a, b, 0.501)));
        Tests.equalHex("hex_round 4", FractionalHex.hexRound(a), FractionalHex.hexRound(new FractionalHex(a.q * 0.4 + b.q * 0.3 + c.q * 0.3, a.r * 0.4 + b.r * 0.3 + c.r * 0.3, a.s * 0.4 + b.s * 0.3 + c.s * 0.3)));
        Tests.equalHex("hex_round 5", FractionalHex.hexRound(c), FractionalHex.hexRound(new FractionalHex(a.q * 0.3 + b.q * 0.3 + c.q * 0.4, a.r * 0.3 + b.r * 0.3 + c.r * 0.4, a.s * 0.3 + b.s * 0.3 + c.s * 0.4)));
    }


    static public void testHexLinedraw()
    {
        Tests.equalHexArray("hex_linedraw", new ArrayList<Hex>(){{add(new Hex(0, 0, 0)); add(new Hex(0, -1, 1)); add(new Hex(0, -2, 2)); add(new Hex(1, -3, 2)); add(new Hex(1, -4, 3)); add(new Hex(1, -5, 4));}}, FractionalHex.hexLinedraw(new Hex(0, 0, 0), new Hex(1, -5, 4)));
    }


    static public void testLayout()
    {
        Hex h = new Hex(3, 4, -7);
        Layout flat = new Layout(Layout.flat, new Point(10, 15), new Point(35, 71));
        Tests.equalHex("layout", h, FractionalHex.hexRound(Layout.pixelToHex(flat, Layout.hexToPixel(flat, h))));
        Layout pointy = new Layout(Layout.pointy, new Point(10, 15), new Point(35, 71));
        Tests.equalHex("layout", h, FractionalHex.hexRound(Layout.pixelToHex(pointy, Layout.hexToPixel(pointy, h))));
    }


    static public void testConversionRoundtrip()
    {
        Hex a = new Hex(3, 4, -7);
        OffsetCoord b = new OffsetCoord(1, -3);
        Tests.equalHex("conversion_roundtrip even-q", a, OffsetCoord.qoffsetToCube(OffsetCoord.EVEN, OffsetCoord.qoffsetFromCube(OffsetCoord.EVEN, a)));
        Tests.equalOffsetcoord("conversion_roundtrip even-q", b, OffsetCoord.qoffsetFromCube(OffsetCoord.EVEN, OffsetCoord.qoffsetToCube(OffsetCoord.EVEN, b)));
        Tests.equalHex("conversion_roundtrip odd-q", a, OffsetCoord.qoffsetToCube(OffsetCoord.ODD, OffsetCoord.qoffsetFromCube(OffsetCoord.ODD, a)));
        Tests.equalOffsetcoord("conversion_roundtrip odd-q", b, OffsetCoord.qoffsetFromCube(OffsetCoord.ODD, OffsetCoord.qoffsetToCube(OffsetCoord.ODD, b)));
        Tests.equalHex("conversion_roundtrip even-r", a, OffsetCoord.roffsetToCube(OffsetCoord.EVEN, OffsetCoord.roffsetFromCube(OffsetCoord.EVEN, a)));
        Tests.equalOffsetcoord("conversion_roundtrip even-r", b, OffsetCoord.roffsetFromCube(OffsetCoord.EVEN, OffsetCoord.roffsetToCube(OffsetCoord.EVEN, b)));
        Tests.equalHex("conversion_roundtrip odd-r", a, OffsetCoord.roffsetToCube(OffsetCoord.ODD, OffsetCoord.roffsetFromCube(OffsetCoord.ODD, a)));
        Tests.equalOffsetcoord("conversion_roundtrip odd-r", b, OffsetCoord.roffsetFromCube(OffsetCoord.ODD, OffsetCoord.roffsetToCube(OffsetCoord.ODD, b)));
    }


    static public void testOffsetFromCube()
    {
        Tests.equalOffsetcoord("offset_from_cube even-q", new OffsetCoord(1, 3), OffsetCoord.qoffsetFromCube(OffsetCoord.EVEN, new Hex(1, 2, -3)));
        Tests.equalOffsetcoord("offset_from_cube odd-q", new OffsetCoord(1, 2), OffsetCoord.qoffsetFromCube(OffsetCoord.ODD, new Hex(1, 2, -3)));
    }


    static public void testOffsetToCube()
    {
        Tests.equalHex("offset_to_cube even-", new Hex(1, 2, -3), OffsetCoord.qoffsetToCube(OffsetCoord.EVEN, new OffsetCoord(1, 3)));
        Tests.equalHex("offset_to_cube odd-q", new Hex(1, 2, -3), OffsetCoord.qoffsetToCube(OffsetCoord.ODD, new OffsetCoord(1, 2)));
    }


    static public void testAll()
    {
        Tests.testHexArithmetic();
        Tests.testHexDirection();
        Tests.testHexNeighbor();
        Tests.testHexDiagonal();
        Tests.testHexDistance();
        Tests.testHexRound();
        Tests.testHexLinedraw();
        Tests.testLayout();
        Tests.testConversionRoundtrip();
        Tests.testOffsetFromCube();
        Tests.testOffsetToCube();
    }


  /*  static public void main(String[] args)
    {
        Tests.testAll();
    }
*/

    static public void complain(String name)
    {
        System.out.println("FAIL " + name);
    }

}
