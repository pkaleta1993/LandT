/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pk.ltgame.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.pk.ltgame.hex.Hex;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.map.TiledMapStage;
import com.pk.ltgame.objects.HexCalculation;
import static java.lang.Thread.sleep;
//import static com.pk.ltgame.map.TiledMapStage.hexNeighbors;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author pkale
 */
public class AIPlayer extends Actor{

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
    private Hex pHex;
    
    private TiledMapStage tMS = new TiledMapStage();
    public static ArrayList<Hex> hexNeighbors = new ArrayList<Hex>();
    private ArrayList<Units> neighborUnits= new ArrayList<Units>();
    private ArrayList<TileBuildings> neighborBuildings= new ArrayList<TileBuildings>();
    public String race;
    /**
     *
     * @param color
     * @param gold
     * @param food
     * @param techpoints
     * @param turn
     */
    public AIPlayer(String color, int gold, int food, float techpoints, int turn, String race) {
        
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
    
    /**
     *
     * @param unitsList
     * @param buildingsList
     * @param mapX
     * @param mapY
     */
    public void makeADecision(ArrayList<Units> unitsList, ArrayList<TileBuildings> buildingsList, int mapX, int mapY, TiledMapStage map){
        //TODO - kolejność podejmowania działań
        // if jednostki wroga potezne niedaleko, to zobacz ile mozna jednostek kupic i czy warto(może lepiej uciekać)
        //if slabe jednostki obok, to atakuj
        // if tile obok, ktory warto atakowac, to atakuj
        //czy warto kupic jednostki
        // czy warto kupic budynek
        System.out.println("makeADecision: Działania gracza AI");
        this.tMS = map;
        int oF = 0;
        ArrayList<Double> buyBuildingCalculations = new ArrayList<Double>();
        ArrayList<Double> buyUnitCalculations = new ArrayList<Double>();
        ArrayList<HexCalculation> attackEnemyCalculations = new ArrayList<HexCalculation>();
        ArrayList<HexCalculation> attackTileCalculations = new ArrayList<HexCalculation>();
        int selectedID = -1;
        for(int i=0;i<unitsList.size();i++)
        {
            if(this.color.equals(new String(unitsList.get(i).playerColor))){
                         //   System.out.println(this.color + " == " + unitsList.get(i).playerColor);
            
            selectedID = unitsList.indexOf(unitsList.get(i));
            buyBuildingCalculations.add(buyBuilding(unitsList.get(i), buildingsList));
            System.out.println("buyBuilding: "+buyBuilding(unitsList.get(i), buildingsList));
            buyUnitCalculations.add(buyUnits(unitsList.get(i), mapX, mapY));
            System.out.println("buyUnits: "+buyUnits(unitsList.get(i),mapX,mapY));
            attackEnemyCalculations.add(attackEnemy(unitsList.get(i), mapX, mapY));
            System.out.println("attackEnemy: "+attackEnemy(unitsList.get(i),mapX,mapY));
            attackTileCalculations.add(attackTile(unitsList.get(i), mapX, mapY));
           System.out.println("attackTile: "+attackTile(unitsList.get(i),mapX,mapY));
            
        
         System.out.println("Jezeli: " +buyBuildingCalculations.get(oF)+ ">= " +buyUnitCalculations.get(oF));
     if (buyBuildingCalculations.get(oF)>=buyUnitCalculations.get(oF)){
        //Kupno budynku
        System.out.println("1. Kupno budynku");
        buyTileBuilding(unitsList.get(i), buildingsList);
        buyUnitCalculations.set(oF, buyUnits(unitsList.get(i), mapX, mapY));
        
        if(buyUnitCalculations.get(oF)>=1) {
            System.out.println("1. Kupno jednostek");
            buyUnit(unitsList.get(i),mapX,mapY);
            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Kupujemy jednostki");
            attackEnemyCalculations.set(oF, attackEnemy(unitsList.get(i),mapX,mapY));
        }
        
     } else {
         //Kupno jednostek
         System.out.println("2. Kupno jednostek");
         buyUnit(unitsList.get(i),mapX,mapY);
          System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Kupujemy jednostki");
          buyBuildingCalculations.set(oF, buyBuilding(unitsList.get(i), buildingsList));
          attackEnemyCalculations.set(oF, attackEnemy(unitsList.get(i),mapX,mapY));
          if(buyBuildingCalculations.get(oF)>=1){
              System.out.println("2. Kupno budynku");
              //Kupno budynku
              buyTileBuilding(unitsList.get(i), buildingsList);
              
          }
         
     }
  
     System.out.println("Jezeli move: " + unitsList.get(i).move+">=1");
     if(unitsList.get(i).move>=1){
         System.out.println("Jezeli: " +attackTileCalculations.get(oF).calculation+ ">= " +attackEnemyCalculations.get(oF).calculation+ " && "+ attackTileCalculations.get(oF).hex );
           System.out.println(attackEnemyCalculations.get(oF).hex );
         if(attackTileCalculations.get(oF).calculation>=attackEnemyCalculations.get(oF).calculation && attackTileCalculations.get(oF).hex != null){
    
             System.out.println("atak na tile");
             tMS.getNeighbors(selectedID);
             tMS.moveUnit(selectedID, attackTileCalculations.get(oF).hex, this.color);
            } else if(attackEnemyCalculations.get(oF).hex != null){
                    System.out.println("hex do ataku nie jest  pusty");
                    System.out.println("Współczynnik ataku: "+ attackEnemyCalculations.get(oF).calculation);
                   if(attackEnemyCalculations.get(oF).calculation>=0.9){
                       //Atakuj
                       System.out.println("tMS.moveUnit("+selectedID+", Hex("+attackEnemyCalculations.get(oF).hex.q+", "+attackEnemyCalculations.get(oF).hex.r+", "+attackEnemyCalculations.get(oF).hex.s+"),"+this.color+");");
                       tMS.getNeighbors(selectedID);
                       tMS.moveUnit(selectedID, attackEnemyCalculations.get(oF).hex, this.color);
                   } else {
                       makeMove(unitsList.get(i), buildingsList, unitsList, mapX, mapY);
                   }
            } else {
                System.out.println("Make move 1");
                       makeMove(unitsList.get(i), buildingsList, unitsList, mapX, mapY);
                   }
     
     }
     oF++;
     }
     }   
    }
    
    private void buyUnit(Units thisUnit, int mapX, int mapY){
        neighborUnits.clear();
       int enemyUnitsPower = 0;
       int thisUnitPower = 0;
       int ableToBuy = 0;
       int maxMelee = maxAbleToBuy(10,3);
       int maxRange = maxAbleToBuy(15,2);
       int maxSpecial = maxAbleToBuy(30,8);
       
        
       getNeighbors(thisUnit, mapX, mapY);
       //System.out.println("Rozmiar hexNeighbors: "+hexNeighbors.size());
       for(int i=0;i<hexNeighbors.size();i++){
          // System.out.println("petla hexNeigh");
           if(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color) != null){
               neighborUnits.add(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color));
             //  System.out.println("Dodaje do units");
           }
       }
       for(int i=0;i<neighborUnits.size();i++){
           enemyUnitsPower = enemyUnitsPower + neighborUnits.get(i).meleeUnits + neighborUnits.get(i).rangeUnits + neighborUnits.get(i).specialUnits*3;
       }
       thisUnitPower += thisUnit.meleeUnits + thisUnit.rangeUnits + thisUnit.specialUnits*3;
      //  System.out.println(thisUnitPower);
     //  System.out.println(enemyUnitsPower);
       while((maxMelee !=0 && maxRange != 0 && maxSpecial!=0) && thisUnitPower<=enemyUnitsPower*1.25)
       {
        maxMelee = maxAbleToBuy(10,3);
        maxRange = maxAbleToBuy(15,2);
        maxSpecial = maxAbleToBuy(30,8);

       
       if(maxMelee >=maxRange && maxMelee >=maxSpecial*4){
          thisUnit.meleeUnits++;
          payFor(10,3);
          thisUnitPower++;
           
       } else if(maxSpecial*4>=maxRange && maxSpecial*4>=maxMelee )
       {
           thisUnit.specialUnits++;
           payFor(30,8);
           thisUnitPower+=3;
       } else {
          thisUnit.rangeUnits++;
          payFor(15,2);
          thisUnitPower++;
       }
       }
      thisUnit.updateArmy();
  

    }
    
    private void buyTileBuilding(Units thisUnit, ArrayList<TileBuildings> buildingsList){
        int goldSummary = getSummaryGold(buildingsList, this.color);
        int foodSummary = getSummaryFood(buildingsList, this.color);
        int goldHumanSummary = getSummaryGold(buildingsList, "RED");
        int foodHumanSummary = getSummaryFood(buildingsList, "RED");
        boolean TileBought = false;
      //  System.out.println("**************if "+goldSummary/3+" >= "+foodSummary);
        if((goldSummary/3)>=foodSummary){
            if(this.gold>=25 && this.food>=10)
            {
                this.payFor(25, 10);
                tMS.addTileBuilding(tMS.tileBuildingI, thisUnit.q, thisUnit.r, thisUnit.s, 40, 40, 5, 3, 0, this.color, "farm", this.race);
                System.out.println("Kupiono farmę");
                thisUnit.move--;
            }
        } else {
            if(this.gold>=15 && this.food>=15)
            {
                this.payFor(15, 15);
                tMS.addTileBuilding(tMS.tileBuildingI, thisUnit.q, thisUnit.r, thisUnit.s, 40, 40, 10, 2, 0, this.color, "craft", this.race);
                System.out.println("Kupiono craft");
                thisUnit.move--;
            }
            
        }
    }
    
    
    private int getDistance(Units thisUnit, Hex hex){
    
      return  Hex.distance(new Hex(thisUnit.q, thisUnit.r, thisUnit.s), hex);
    }
    
    private Hex getHexToGetTo(Units thisUnit, Hex start, Hex end, int mapX, int mapY)
    {
        int checkpointQ = 0;
        int checkpointR = 0;
        int checkpointS = 0;
        getNeighbors(thisUnit,mapX,mapY);
        if(start.q == end.q)
        {
            checkpointQ = start.q;
            if(start.r>end.r)
            {
                checkpointR = start.r-1;
                checkpointS = start.s+1;
            } else {
                checkpointR = start.r+1;
                checkpointS = start.s-1;
            }
        } else if(start.q!=end.q)
        {
            if(start.q<end.q)
            {
                checkpointQ = start.q+1;
                if((end.r - start.r-1)>(end.r-start.r))
                {
                    checkpointR = start.r-1;
                    checkpointS = start.s;
                    
                } else {
                    checkpointS = start.s-1;
                    checkpointR = start.r;
                }
            } else {
                checkpointQ = start.q-1;
                if((end.r - start.r+1)>(end.r-start.r))
                {
                    checkpointR = start.r+1;
                    checkpointS = start.s;
                    
                } else {
                    checkpointS = start.s+1;
                    checkpointR = start.r;
                }
                
            }
            
           
        }
        
        return new Hex(checkpointQ, checkpointR, checkpointS);
    }
    
    private void makeMove(Units thisUnit, ArrayList<TileBuildings> buildingsList, ArrayList<Units> unitsList, int mapX, int mapY){
    int goldSummary = getSummaryGold(buildingsList, this.color);
        int foodSummary = getSummaryFood(buildingsList, this.color);
        int goldHumanSummary = getSummaryGold(buildingsList, "RED");
        int foodHumanSummary = getSummaryFood(buildingsList, "RED");
        
        int randomInt = 0;
        double getProduction = 1-(((goldSummary/(goldSummary+goldHumanSummary)) +(foodSummary/(foodSummary+foodHumanSummary)) )/2); //Puste pole
        int getAttackUnits = 0; //(thisUnit.meleeUnits+thisUnit.rangeUnits+thisUnit.specialUnits*3); //Wroga jednostka
        int getHumanAttackUnits = 0;
        int closest = 1000;
        Hex closestHex = null;
        double getArmyAttack = 0;
        double getArmyConquer = 0;
        int selectedID = unitsList.indexOf(thisUnit);
        for(int i=0;i<unitsList.size();i++){
            if(unitsList.get(i).playerColor != "RED"){
                getAttackUnits += unitsList.get(i).meleeUnits+unitsList.get(i).rangeUnits+unitsList.get(i).specialUnits*3;
            } else getHumanAttackUnits += unitsList.get(i).meleeUnits+unitsList.get(i).rangeUnits+unitsList.get(i).specialUnits*3;
        }
         getArmyAttack=(getAttackUnits/(getHumanAttackUnits+getAttackUnits));
        getArmyConquer = ((goldHumanSummary/(goldSummary+goldHumanSummary)) +(foodHumanSummary/(foodSummary+foodHumanSummary)) )/2;
        if(getHumanAttackUnits != 0 && getProduction>=getArmyConquer && getProduction>=getArmyAttack){
        //szukamy najblizszego wolnego pola
        System.out.println("Szukamy wolnego pola");
        for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && !(tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "farm") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "craft") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "castle")))
                    {
                       
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
                       // if(closest ==1) closestHexList.add(closestHex);
                    }
                }
            }
        if(closestHex == null){
            closest = getDistance(thisUnit, new Hex(buildingsList.get(0).q, buildingsList.get(0).r, buildingsList.get(0).s));
            closestHex = new Hex(buildingsList.get(0).q, buildingsList.get(0).r, buildingsList.get(0).s);
        }
        if(closest>1){
           closestHex = getHexToGetTo(thisUnit, new Hex(thisUnit.q, thisUnit.r, thisUnit.s), closestHex, mapX, mapY);
        }
        
        System.out.println("Jednostka Hex: ("+thisUnit.q+", "+thisUnit.r+", "+thisUnit.s+")");
       System.out.println("moveUnit("+selectedID+", Hex("+closestHex.q+", "+closestHex.r+", "+closestHex.s+"), "+this.color+");");
        tMS.getNeighbors(selectedID);
        tMS.moveUnit(selectedID, closestHex, this.color);
        
        
        } else if(getArmyConquer >= getProduction && getArmyConquer >= getArmyAttack){
            //szukamy najblizszego pola z wrogim budynkiem
                    System.out.println("Szukamy pola z budynkiem");

            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && (tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "farm") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "craft") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "castle")))
                    {
                       
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
                       // if(closest ==1) closestHexList.add(closestHex);
                    }
                }
            }
            
             if(closestHex == null){
            closest = getDistance(thisUnit, new Hex(buildingsList.get(0).q, buildingsList.get(0).r, buildingsList.get(0).s));
            closestHex = new Hex(buildingsList.get(0).q, buildingsList.get(0).r, buildingsList.get(0).s);
        }
             
        if(closest>1){
           closestHex = getHexToGetTo(thisUnit, new Hex(thisUnit.q, thisUnit.r, thisUnit.s), closestHex, mapX, mapY);
        }
        tMS.getNeighbors(selectedID);
        tMS.moveUnit(selectedID, closestHex, this.color);
        
        
        } else {
            //szukamy najblizszego pola z armią wroga
                    System.out.println("Szukamy z wroga armia");

             for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    
                     if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && tMS.containTileEnemyUnit(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), this.color) !=null)
                    {
                       
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
                       // if(closest ==1) closestHexList.add(closestHex);
                    }
                }
            }
                     System.out.println("1Jednostka Q: "+thisUnit.q +" R: "+ thisUnit.r + " S: "+ thisUnit.s);
        //System.out.println("1closestHex Q: "+closestHex.q +" R: "+ closestHex.r + " S: "+ closestHex.s);
              if(closestHex == null){
            closest = getDistance(thisUnit, new Hex(buildingsList.get(0).q, buildingsList.get(0).r, buildingsList.get(0).s));
            closestHex = new Hex(buildingsList.get(0).q, buildingsList.get(0).r, buildingsList.get(0).s);
        }
              System.out.println("2Jednostka Q: "+thisUnit.q +" R: "+ thisUnit.r + " S: "+ thisUnit.s);
        System.out.println("2closestHex Q: "+closestHex.q +" R: "+ closestHex.r + " S: "+ closestHex.s);
        if(closest>1){
           closestHex = getHexToGetTo(thisUnit, new Hex(thisUnit.q, thisUnit.r, thisUnit.s), closestHex, mapX, mapY);
        }
        System.out.println("3closestHex = "+closestHex);
        System.out.println("3Jednostka Q: "+thisUnit.q +" R: "+ thisUnit.r + " S: "+ thisUnit.s);
        System.out.println("3closestHex Q: "+closestHex.q +" R: "+ closestHex.r + " S: "+ closestHex.s);
        tMS.getNeighbors(selectedID);
        tMS.moveUnit(selectedID, closestHex, this.color);
        
        
        }
            
    }
    /*
    private void makeMove(Units thisUnit, ArrayList<TileBuildings> buildingsList, ArrayList<Units> unitsList, int mapX, int mapY){
      // System.out.println("Jest w makeMove([...])");
        int goldSummary = getSummaryGold(buildingsList, this.color);
        int foodSummary = getSummaryFood(buildingsList, this.color);
        int goldHumanSummary = getSummaryGold(buildingsList, "RED");
        int foodHumanSummary = getSummaryFood(buildingsList, "RED");
        int selectedID = -1;
        int randomInt = 0;
        double getProduction = 1-(((goldSummary/(goldSummary+goldHumanSummary)) +(foodSummary/(foodSummary+foodHumanSummary)) )/2); //Puste pole
        int getAttackUnits = 0; //(thisUnit.meleeUnits+thisUnit.rangeUnits+thisUnit.specialUnits*3); //Wroga jednostka
        int getHumanAttackUnits = 0;
        double getArmyAttack = 0;
        double getArmyConquer = 0;
        selectedID = unitsList.indexOf(thisUnit);
        for(int i=0;i<unitsList.size();i++){
            if(unitsList.get(i).playerColor != "RED"){
                getAttackUnits += unitsList.get(i).meleeUnits+unitsList.get(i).rangeUnits+unitsList.get(i).specialUnits*3;
            } else getHumanAttackUnits += unitsList.get(i).meleeUnits+unitsList.get(i).rangeUnits+unitsList.get(i).specialUnits*3;
        }
        getArmyAttack=(getAttackUnits/(getHumanAttackUnits+getAttackUnits));
        getArmyConquer = ((goldHumanSummary/(goldSummary+goldHumanSummary)) +(foodHumanSummary/(foodSummary+foodHumanSummary)) )/2;
        if(getProduction>=getArmyConquer && getProduction>=getArmyAttack){
            
            // szukamy najblizszego wolnego pola
            int closest = 100;
            Hex closestHex = null;
           // System.out.println("FOR");
            ArrayList<Hex> closestHexList = new ArrayList<Hex>();
            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && !(tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "farm") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "craft") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "castle")))
                    {
                       // System.out.println(getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s)));
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
                        if(closest ==1) closestHexList.add(closestHex);
                    }
                }
            }
           /* if(closest >1){
                if(closestHex.q >thisUnit.q){
                    thisUnit.q++;
                    if(closestHex.r>thisUnit.r){
                        thisUnit.s--;
                    } else thisUnit.r--;
                }
            } /////// Do zakomentowania
           
            System.out.println("Q: "+ closestHex.q + "R: "+ closestHex.r+"s: "+ closestHex.s);
            tMS.getNeighbors(selectedID);
            if(closest==1){
            int closestHexSize = closestHexList.size();
            Random rand = new Random();
            randomInt = rand.nextInt(closestHexSize-1);
            tMS.moveUnit(selectedID, closestHexList.get(randomInt), this.color);
           
            
             } else {
                
             int minusQ = thisUnit.q-closestHex.q;
            int minusR = thisUnit.r-closestHex.r;
            int minusS = thisUnit.s-closestHex.s;
            Hex hex = null;
            for(int i = 0;i<hexNeighbors.size();i++)
            {
            
                hex = tMS.hexNeighbors.get(i);
                if(tMS.containCubeArr(hex.q,hex.r,hex.s) && tMS.containTileUnit(hex, this.color) != null){
                i=hexNeighbors.size()-1;
                }
            }
             tMS.moveUnit(selectedID, hex, this.color);
           
            }
            tMS.selectedUnit = -1;
        } else if(getArmyConquer >= getProduction && getArmyConquer >= getArmyAttack){
            //szukamy najblizszego pola z wrogim budynkiem
            
             int closest = 100;
            Hex closestHex = null;
            System.out.println("FOR");
            ArrayList<Hex> closestHexList = new ArrayList<Hex>();
            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && (tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "farm") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "craft") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "castle")))
                    {
                        System.out.println(getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s)));
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
                        if(closest ==1) closestHexList.add(closestHex);
                    }
                }
            }
           /* if(closest >1){
                if(closestHex.q >thisUnit.q){
                    thisUnit.q++;
                    if(closestHex.r>thisUnit.r){
                        thisUnit.s--;
                    } else thisUnit.r--;
                }
            } // do zakomentowania
            System.out.println("Q: "+ closestHex.q + "R: "+ closestHex.r+"s: "+ closestHex.s);
            tMS.getNeighbors(selectedID);
            if(closest==1){
            int closestHexSize = closestHexList.size();
            Random rand = new Random();
            randomInt = rand.nextInt(closestHexSize-1);
            tMS.moveUnit(selectedID, closestHexList.get(randomInt), this.color);
           
            
             } else {
                
             int minusQ = thisUnit.q-closestHex.q;
            int minusR = thisUnit.r-closestHex.r;
            int minusS = thisUnit.s-closestHex.s;
            Hex hex = null;
            for(int i = 0;i<hexNeighbors.size();i++)
            {
            
                hex = tMS.hexNeighbors.get(i);
                if(tMS.containCubeArr(hex.q,hex.r,hex.s) && tMS.containTileUnit(hex, this.color) != null){
                i=hexNeighbors.size()-1;
                }
            }
             tMS.moveUnit(selectedID, hex, this.color);
           
            }
            tMS.selectedUnit = -1;
            
            
        } else {
            //szukamy najblizszego pola z armią wroga
            
            
            
            
            // szukamy najblizszego wolnego pola
            int closest = 100;
            Hex closestHex = null;
            System.out.println("FOR");
            ArrayList<Hex> closestHexList = new ArrayList<Hex>();
            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && tMS.containTileEnemyUnit(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), this.color) !=null)
                    {
                        System.out.println(getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s)));
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
                        if(closest ==1) closestHexList.add(closestHex);
                    }
                }
            }
           /* if(closest >1){
                if(closestHex.q >thisUnit.q){
                    thisUnit.q++;
                    if(closestHex.r>thisUnit.r){
                        thisUnit.s--;
                    } else thisUnit.r--;
                }
            } // do zakomentowania
            System.out.println("Q: "+ closestHex.q + "R: "+ closestHex.r+"s: "+ closestHex.s);
            tMS.getNeighbors(selectedID);
            if(closest==1){
            int closestHexSize = closestHexList.size();
            Random rand = new Random();
            randomInt = rand.nextInt(closestHexSize-1);
            tMS.moveUnit(selectedID, closestHexList.get(randomInt), this.color);
           
            
             } else {
                
             int minusQ = thisUnit.q-closestHex.q;
            int minusR = thisUnit.r-closestHex.r;
            int minusS = thisUnit.s-closestHex.s;
            Hex hex = null;
            for(int i = 0;i<hexNeighbors.size();i++)
            {
            
                hex = tMS.hexNeighbors.get(i);
                if(tMS.containCubeArr(hex.q,hex.r,hex.s) && tMS.containTileUnit(hex, this.color) != null){
                i=hexNeighbors.size()-1;
                }
            }
             tMS.moveUnit(selectedID, hex, this.color);
           
            }
            tMS.selectedUnit = -1;
        }
    
    }
    */
    /**
     *
     * @param thisUnit
     * @param enemyUnit
     * @param mapX
     * @param mapY
     * @return
     */
    public HexCalculation attackEnemy(Units thisUnit, int mapX, int mapY){
        //TODO - czy atakować enemyUnit jednostką thisUnit
         
        double calculation = 0;
        HexCalculation calcHex = new HexCalculation(null, calculation);
        int enemyUnitsPower = 0;
        int thisUnitPower = thisUnit.meleeUnits+thisUnit.rangeUnits+thisUnit.specialUnits*3;
        getNeighbors(thisUnit, mapX, mapY);
       for(int i=0;i<hexNeighbors.size();i++){
           
           if(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color) != null){
               neighborUnits.add(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color));
               calcHex = new HexCalculation(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), calculation);
           }
       }
       
       for(int i=0;i<neighborUnits.size();i++){
           enemyUnitsPower = enemyUnitsPower + neighborUnits.get(i).meleeUnits + neighborUnits.get(i).rangeUnits + neighborUnits.get(i).specialUnits*3;
       }
       if(thisUnitPower>=enemyUnitsPower*0.9 && enemyUnitsPower>0) {
           calculation += (thisUnitPower/enemyUnitsPower);
       } //
       //else calculation -= 0.3;
       calcHex.calculation = calculation;
    return calcHex;
    }
    
    /**
     *
     * @param thisUnit
     * @param enemyTile
     * @return
     */
    public HexCalculation attackTile(Units thisUnit, int mapX, int mapY){
        //TODO - czy atakować tile przeciwnika jednostką thisUnit
        neighborBuildings.clear();
        double calculation = 3;
        HexCalculation calcHex = new HexCalculation(null, calculation);
        
        int conquerPower = thisUnit.meleeUnits+thisUnit.rangeUnits+thisUnit.specialUnits*4;
        getNeighbors(thisUnit, mapX, mapY);
        for(int i=0;i<hexNeighbors.size();i++){
           
           if(tMS.containTileEnemyBuildingObject(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color) != null){
               neighborBuildings.add(tMS.containTileEnemyBuildingObject(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color));
                calcHex = new HexCalculation(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), calculation);
           }
       }
        for(int i=0;i<neighborBuildings.size();i++)
        {
            
              //calculation -= (neighborBuildings.get(i).HP/conquerPower);
              calcHex.calculation = calculation - (neighborBuildings.get(i).HP/conquerPower);
              return calcHex;
            
        }
        return calcHex;
    }

    /**
     *
     * @param thisUnit
     * @param mapX
     * @param mapY
     * @return
     */
    public double buyUnits(Units thisUnit, int mapX, int mapY)
    {
       neighborUnits.clear();
       int enemyUnitsPower = 0;
       int thisUnitPower = 0;
       int ableToBuy = 0;
       int maxMelee = 0;
       int maxRange = 0;
       int maxSpecial = 0;
       double calculation = 0.5; 
        
       getNeighbors(thisUnit, mapX, mapY);
       //System.out.println("Rozmiar hexNeighbors: "+hexNeighbors.size());
       for(int i=0;i<hexNeighbors.size();i++){
          // System.out.println("petla hexNeigh");
           if(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color) != null){
               neighborUnits.add(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color));
             //  System.out.println("Dodaje do units");
           }
       }
       for(int i=0;i<neighborUnits.size();i++){
           enemyUnitsPower = enemyUnitsPower + neighborUnits.get(i).meleeUnits + neighborUnits.get(i).rangeUnits + neighborUnits.get(i).specialUnits*3;
       }
       thisUnitPower += thisUnit.meleeUnits + thisUnit.rangeUnits + thisUnit.specialUnits*3;
      //  System.out.println(thisUnitPower);
     //  System.out.println(enemyUnitsPower);
       if(thisUnitPower<enemyUnitsPower*0.85) {
       
      
       maxMelee = maxAbleToBuy(10,3);
       System.out.println("///MaxMelee = " + maxAbleToBuy(10,3));
       maxRange = maxAbleToBuy(15,2);
       System.out.println("///MaxRange = " + maxAbleToBuy(15,2));
       maxSpecial = maxAbleToBuy(30,8);
       System.out.println("///MaxSpecial = " + maxAbleToBuy(30,8));
       
       if(maxMelee >=maxRange && maxMelee >=maxSpecial*4){
          
               if(recalcPower(enemyUnitsPower, thisUnitPower, maxMelee, 0, 0))
               {
                   //thisUnit.specialUnits += maxSpecial;
                   //payFor(30*maxSpecial,8*maxSpecial);
                   calculation += 1;
               }
           
       } else if(maxSpecial*4>=maxRange && maxSpecial*4>=maxMelee )
       {
           if(recalcPower(enemyUnitsPower, thisUnitPower, 0, 0, maxSpecial))
               {
                  // thisUnit.rangeUnits += maxRange;
                  // payFor(15*maxRange,2*maxRange);
                   calculation += 1;
               }
       } else {
           if(recalcPower(enemyUnitsPower, thisUnitPower, 0, maxRange, 0))
               {
                  // thisUnit.rangeUnits += maxRange;
                  // payFor(15*maxRange,2*maxRange);
                   calculation += 1;
               }
       }
    } else calculation -= 0.3;
       
       return calculation;
    }
    
 private boolean recalcPower(int enemyPower, int allyPower, int melee, int range, int special){
    allyPower+= melee+range+special*3;
    System.out.println("////AllyPower = allyPower:  " + allyPower + " VS " +enemyPower*0.85);
        return allyPower >= enemyPower*0.85;
 }
 private int getMax(int goldCost, int foodCost){
     int maxGold = (int)(this.gold/goldCost);
     int maxFood = (int)(this.food/foodCost);
     if(maxGold<=maxFood) return maxGold; else return maxFood;
 }
 
 private int maxAbleToBuy(int goldCost, int foodCost){
    int max = 0;
    max = getMax(goldCost,foodCost);
     
  return max;
 }
 
 public void getNeighbors(Units thisUnit, int mapX, int mapY){
        pHex = new Hex(thisUnit.q,thisUnit.r,thisUnit.s);
        hexNeighbors.clear();
        neighborUnits.clear();
      // System.out.println("thisUnit w neighbors "+thisUnit.q);
       for(int i=0; i<6;i++)
         {
       //System.out.println("Rozmiar mapy: "+tMS.mapXCells);
          // System.out.println("Q: "+Hex.neighbor(pHex, i).q+"R: "+Hex.neighbor(pHex, i).r+"S: "+ Hex.neighbor(pHex, i).s);
         if(tMS.containCubeArr(Hex.neighbor(pHex, i).q,Hex.neighbor(pHex, i).r,Hex.neighbor(pHex, i).s) == true){
            // System.out.println(" ");
             hexNeighbors.add(Hex.neighbor(pHex, i));
             
             //System.out.println("Rozmiar: "+hexNeighbors.size());

         }
      
         }
    }
    /**
     *
     * @param thisUnit
     * @param buildingsList
     * @return
     */
    public int getSummaryGold(ArrayList<TileBuildings> buildingsList, String color){
        int goldSummary = 0;
        for(int i=0;i<buildingsList.size();i++){
            if(buildingsList.get(i).playerColor.equals(new String(color)))
            {
                goldSummary += buildingsList.get(i).dayGold;
            }
        }
       // System.out.println("^^^^^^^^Zwracam gold: " + goldSummary);
        return goldSummary;
    }
     public int getSummaryFood(ArrayList<TileBuildings> buildingsList, String color){
        int foodSummary = 0;
        for(int i=0;i<buildingsList.size();i++){
            if(buildingsList.get(i).playerColor.equals(new String(color)))
            {
                foodSummary += buildingsList.get(i).dayFood;
            }
        }
        //System.out.println("^^^^^^^^Zwracam food: " + foodSummary);
        return foodSummary;
    }
    
    public double buyBuilding(Units thisUnit, ArrayList<TileBuildings> buildingsList){
        //TODO - czy kupować budynek
        boolean buildingExist = false;
        double calculation = 0.5;
        int goldSummary = getSummaryGold(buildingsList, this.color);
        int foodSummary = getSummaryFood(buildingsList, this.color);
        int goldHumanSummary = getSummaryGold(buildingsList, "RED");
        int foodHumanSummary = getSummaryFood(buildingsList, "RED");
        
        for(int i=0;i<buildingsList.size();i++){
            if(thisUnit.q == buildingsList.get(i).q && thisUnit.r == buildingsList.get(i).r && thisUnit.s == buildingsList.get(i).s){
                buildingExist = true;
            }
            
        }
       // System.out.println(buildingExist);
        if(buildingExist == false){
            if(goldSummary<=goldHumanSummary){
                calculation += (goldHumanSummary-goldSummary)*0.1;
            }
            if(foodSummary<=foodHumanSummary){
                calculation += (foodHumanSummary-foodSummary)*0.25;
            }
        } else calculation -= 0.5;
        return calculation;
    }
    
}
