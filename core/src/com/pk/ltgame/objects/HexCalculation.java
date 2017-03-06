/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.objects;

import com.pk.ltgame.hex.Hex;

/**
 *
 * @author pkale
 */
public class HexCalculation {
    
    public final Hex hex;
    public double calculation;
    
    public HexCalculation(Hex hex, double calculation){
    this.hex = hex;
    this.calculation = calculation;    
    }
}
