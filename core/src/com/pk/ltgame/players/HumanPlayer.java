/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.players;


import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;

/**
 *
 * @author pkale
 */
public class HumanPlayer extends Actor {
    
    /**
     *
     */
    public String color;

    /**
     *
     */
    public int gold;

    /**
     *
     */
    public int food;

    /**
     *
     */
    public float techpoints;

    /**
     *
     */
    public int turn;
    public String race;
    public  ArrayList<Boolean> technology;
    /**
     *
     * @param color
     * @param gold
     * @param food
     * @param techpoints
     * @param turn
     */
    public HumanPlayer(String color, int gold, int food, float techpoints, int turn, String race, ArrayList<Boolean> tech) {
        this.technology = tech;
        this.color = color;
        this.gold = gold;
        this.food = food;
        this.techpoints = techpoints;
        this.turn = turn;
        this.race = race;
    }

    /**
     *
     * @param gold
     * @param food
     */
    public void payFor(int gold, int food)
    {
        this.gold = this.gold - gold;
        this.food = this.food - food;
    }
    
  
}
