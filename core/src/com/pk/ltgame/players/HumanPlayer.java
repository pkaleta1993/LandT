/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.players;


import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 *
 * @author pkale
 */
public class HumanPlayer extends Actor {
    
    public String color;
    public int gold;
    public int food;
    public int techpoints;
    public int turn;
    public HumanPlayer(String color, int gold, int food, int techpoints, int turn) {
        
        this.color = color;
        this.gold = gold;
        this.food = food;
        this.techpoints = techpoints;
        this.turn = turn;
    }

  
}
