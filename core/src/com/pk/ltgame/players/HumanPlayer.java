package com.pk.ltgame.players;

import com.badlogic.gdx.scenes.scene2d.Actor;
import java.util.ArrayList;

/**
 * Klasa gracza.
 * @author pkale
 */
public class HumanPlayer extends Actor {
    
    /**
     * Kolor gracza.
     */
    public String color;

    /**
     * Złoto gracza.
     */
    public int gold;

    /**
     * Jedzenie gracza.
     */
    public int food;

    /**
     * Punkty nauki gracza.
     */
    public float techpoints;

    /**
     * Tura gracza.
     */
    public int turn;

    /**
     * Rasa gracza.
     */
    public String race;

    /**
     * Lista odkrytych technologii.
     */
    public  ArrayList<Boolean> technology;
    
    /**
     * Tworzenie obiektu gracza.
     * @param color Kolor gracza.
     * @param gold Ilość złota.
     * @param food Ilość jedzenia.
     * @param techpoints Punkty nauki.
     * @param turn Tura gracza.
     * @param race Rasa gracza.
     * @param tech Lista technologii.
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
     * Odejmowanie złota i jedzenia.
     * @param gold Ilość złota.
     * @param food Ilość jedzenia.
     */
    public void payFor(int gold, int food)
    {
        this.gold = this.gold - gold;
        this.food = this.food - food;
    }

    /**
     * Odejmij punkty nauki.
     * @param techpoints Punkty nauki.
     */
    public void payByTechPoints(float techpoints){
        this.techpoints -= techpoints;
    }
  
}
