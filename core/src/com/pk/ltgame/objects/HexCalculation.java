package com.pk.ltgame.objects;

import com.pk.ltgame.hex.Hex;

/**
 * Klasa pomocnicza do trzymania wartości obliczeń i pola dla którego obliczenia zostały wykonane.
 * @author pkale
 */
public class HexCalculation {
    
    /**
     * Obiekt Hex.
     */
    public final Hex hex;

    /**
     * Wartość obliczeń.
     */
    public double calculation;
    
    /**
     * Zachowuje dwa obiekty(typ Hex i float) jako jeden obiekt.
     * @param hex Obiekt pola dla obliczeń.
     * @param calculation Obliczenia dla pola.
     */
    public HexCalculation(Hex hex, double calculation){
    this.hex = hex;
    this.calculation = calculation;    
    }
}
