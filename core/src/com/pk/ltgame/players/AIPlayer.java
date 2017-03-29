package com.pk.ltgame.players;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.pk.ltgame.hex.Hex;
import com.pk.ltgame.objects.TileBuildings;
import com.pk.ltgame.objects.Units;
import com.pk.ltgame.map.TiledMapStage;
import com.pk.ltgame.objects.HexCalculation;
import java.util.ArrayList;

/**
 * Klasa odpowiedzialna za gracza AI i jego zachowanie.
 * @author pkale
 */
public class AIPlayer extends Actor{

    /**
     *Kolor gracza.
     */
    public String color;

    /**
     * Ilość jednostek złota gracza.
     */
    public int gold;

    /**
     * Ilość jednostek jedzenia gracza.
     */
    public int food;

    /**
     * Ilość punktów nauki gracza.
     */
    public float techpoints;

    /**
     * Tura gracza.
     */
    public int turn;
    private Hex pHex;
    
    private TiledMapStage tMS = new TiledMapStage();

    /**
     * Lista sąsiadnich pól jednostki.
     */
    public static ArrayList<Hex> hexNeighbors = new ArrayList<Hex>();
    private final ArrayList<Units> neighborUnits= new ArrayList<Units>();
    private final ArrayList<TileBuildings> neighborBuildings= new ArrayList<TileBuildings>();

    /**
     * Rasa gracza.
     */
    public String race;
    
    /**
     * Tworzenie obiektu gracza.
     * @param color Kolor gracza.
     * @param gold Złoto gracza
     * @param food Jedzenie gracza.
     * @param techpoints Punkty nauki gracza.
     * @param turn Tura gracza.
     * @param race Rasa gracza.
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
     * Zmniejszenie ilości złota i jedzenia o podaną ilość.
     * @param gold Ilość złota.
     * @param food Ilość jedzenia.
     */
    public void payFor(int gold, int food)
    {
        this.gold = this.gold - gold;
        this.food = this.food - food;
    }
    
    /**
     * Podejmowanie decyzji i wykonanie dla nich akcji.
     * @param unitsList Lista jednostek.
     * @param buildingsList Lista budynków.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     * @param map Mapa.
     */
    public void makeADecision(ArrayList<Units> unitsList, ArrayList<TileBuildings> buildingsList, int mapX, int mapY, TiledMapStage map){
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
                selectedID = unitsList.indexOf(unitsList.get(i));
                buyBuildingCalculations.add(buyBuilding(unitsList.get(i), buildingsList));
                System.out.println("buyBuilding: "+buyBuilding(unitsList.get(i), buildingsList));
                buyUnitCalculations.add(buyUnits(unitsList.get(i), mapX, mapY));
                System.out.println("buyUnits: "+buyUnits(unitsList.get(i),mapX,mapY));
                attackEnemyCalculations.add(attackEnemy(unitsList.get(i), mapX, mapY));
                System.out.println("attackEnemy: "+attackEnemy(unitsList.get(i),mapX,mapY));
                attackTileCalculations.add(attackTile(unitsList.get(i), mapX, mapY));
                if (buyBuildingCalculations.get(oF)>=buyUnitCalculations.get(oF)){
                    buyTileBuilding(unitsList.get(i), buildingsList);
                    buyUnitCalculations.set(oF, buyUnits(unitsList.get(i), mapX, mapY));
                    if(buyUnitCalculations.get(oF)>=1) {
                        buyUnit(unitsList.get(i),mapX,mapY);
                        attackEnemyCalculations.set(oF, attackEnemy(unitsList.get(i),mapX,mapY));
                    }
                } else {
                    buyUnit(unitsList.get(i),mapX,mapY);
                    buyBuildingCalculations.set(oF, buyBuilding(unitsList.get(i), buildingsList));
                    attackEnemyCalculations.set(oF, attackEnemy(unitsList.get(i),mapX,mapY));
                    if(buyBuildingCalculations.get(oF)>=1){
                        buyTileBuilding(unitsList.get(i), buildingsList);
                    }
                }
            if(unitsList.get(i).move>=1){
                if(attackTileCalculations.get(oF).calculation>=attackEnemyCalculations.get(oF).calculation && attackTileCalculations.get(oF).hex != null){
                    tMS.getNeighbors(selectedID);
                    tMS.moveUnit(selectedID, attackTileCalculations.get(oF).hex, this.color);
                } else if(attackEnemyCalculations.get(oF).hex != null){
                    if(attackEnemyCalculations.get(oF).calculation>=0.9){
                        tMS.getNeighbors(selectedID);
                        tMS.moveUnit(selectedID, attackEnemyCalculations.get(oF).hex, this.color);
                    } else {
                        makeMove(unitsList.get(i), buildingsList, unitsList, mapX, mapY);
                    }
                } else {
                    makeMove(unitsList.get(i), buildingsList, unitsList, mapX, mapY);
                }
            }
            oF++;
            }
        }   
    }
    
    /**
     * Zakup jednostek 
     * @param thisUnit Jednostka.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     */
    private void buyUnit(Units thisUnit, int mapX, int mapY){
        neighborUnits.clear();
        int enemyUnitsPower = 0;
        int thisUnitPower = 0;
        int ableToBuy = 0;
        int maxMelee = maxAbleToBuy(10,3);
        int maxRange = maxAbleToBuy(15,2);
        int maxSpecial = maxAbleToBuy(30,8);
        getNeighbors(thisUnit, mapX, mapY);
        for(int i=0;i<hexNeighbors.size();i++){
            if(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color) != null){
                neighborUnits.add(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color));
            }
        }
        for(int i=0;i<neighborUnits.size();i++){
            enemyUnitsPower = enemyUnitsPower + neighborUnits.get(i).meleeUnits + neighborUnits.get(i).rangeUnits + neighborUnits.get(i).specialUnits*3;
        }
        thisUnitPower += thisUnit.meleeUnits + thisUnit.rangeUnits + thisUnit.specialUnits*3;
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
    
    /**
     * Zakup ulepszeń pól.
     * @param thisUnit Jednostka.
     * @param buildingsList Lista budynków.
     */
    private void buyTileBuilding(Units thisUnit, ArrayList<TileBuildings> buildingsList){
        int goldSummary = getSummaryGold(buildingsList, this.color);
        int foodSummary = getSummaryFood(buildingsList, this.color);
        int goldHumanSummary = getSummaryGold(buildingsList, "RED");
        int foodHumanSummary = getSummaryFood(buildingsList, "RED");
        boolean TileBought = false;
        if((goldSummary/3)>=foodSummary){
            if(this.gold>=25 && this.food>=10)
            {
                this.payFor(25, 10);
                tMS.addTileBuilding(tMS.tileBuildingI, thisUnit.q, thisUnit.r, thisUnit.s, 40, 40, 5, 3, 0, this.color, "farm", this.race);
                thisUnit.move--;
            }
        } else {
            if(this.gold>=15 && this.food>=15)
            {
                this.payFor(15, 15);
                tMS.addTileBuilding(tMS.tileBuildingI, thisUnit.q, thisUnit.r, thisUnit.s, 40, 40, 10, 2, 0, this.color, "craft", this.race);
                thisUnit.move--;
            }
            
        }
    }
    
    /**
     * Zmierzenie dystansu do podanego Hexa.
     * @param thisUnit Jednostka.
     * @param hex Docelowy Hex.
     * @return Odległość.
     */
    private int getDistance(Units thisUnit, Hex hex){
        return  Hex.dis(new Hex(thisUnit.q, thisUnit.r, thisUnit.s), hex);
    }
    
    /**
     * Znajdź Hex, który jest na drodze do docelowego Hexa.
     * @param thisUnit Jednostka.
     * @param start Hex początkowy.
     * @param end Hex końcowy.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     * @return Hex w drodze do celu.
     */
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
    
    /**
     * Wykonanie ruchu.
     * @param thisUnit Jednostka.
     * @param buildingsList Lista budynków.
     * @param unitsList Lista jednostek.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     */
    private void makeMove(Units thisUnit, ArrayList<TileBuildings> buildingsList, ArrayList<Units> unitsList, int mapX, int mapY){
        int goldSummary = getSummaryGold(buildingsList, this.color);
        int foodSummary = getSummaryFood(buildingsList, this.color);
        int goldHumanSummary = getSummaryGold(buildingsList, "RED");
        int foodHumanSummary = getSummaryFood(buildingsList, "RED");
        int randomInt = 0;
        double getProduction = 1-(((goldSummary/(goldSummary+goldHumanSummary)) +(foodSummary/(foodSummary+foodHumanSummary)) )/2);
        int getAttackUnits = 0;
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
            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && !(tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "farm") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "craft") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "castle")))
                    { 
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
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
        } else if(getArmyConquer >= getProduction && getArmyConquer >= getArmyAttack){
            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && (tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "farm") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "craft") || tMS.containTileBuilding(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), "castle")))
                    {
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
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
            for(int x=0;x<mapX;x++){
                for(int y=0;y<mapY;y++)
                {
                    if((getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))<=closest ) && (getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s))>0 ) && tMS.containTileEnemyUnit(new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s), this.color) !=null)
                    {
                        closest = getDistance(thisUnit, new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s));
                        closestHex = new Hex(tMS.cubeArr[x][y].q,tMS.cubeArr[x][y].r,tMS.cubeArr[x][y].s);
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
        }  
    }
   
    /**
     * Obliczenia dla atakowania wrogich jednostek.
     * @param thisUnit Jednostka.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     * @return Obliczenia dla ataku.
     */
    public HexCalculation attackEnemy(Units thisUnit, int mapX, int mapY){
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
        }
        calcHex.calculation = calculation;
        
        return calcHex;
    }
    
    /**
     * Obliczenia dla atakowania budynku.
     * @param thisUnit Jednostka.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     * @return Obliczenia dla ataku.
     */
    public HexCalculation attackTile(Units thisUnit, int mapX, int mapY){
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
            calcHex.calculation = calculation - (neighborBuildings.get(i).HP/conquerPower);
            return calcHex;
        }
        
        return calcHex;
    }

    /**
     * Obliczenia dla zakupu jednostek.
     * @param thisUnit Jednostka.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     * @return Obliczenia do zakupu jednostek.
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
        for(int i=0;i<hexNeighbors.size();i++){
            if(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color) != null){
                neighborUnits.add(tMS.containTileEnemyUnit(new Hex(hexNeighbors.get(i).q, hexNeighbors.get(i).r, hexNeighbors.get(i).s), this.color));
            }
        }
        for(int i=0;i<neighborUnits.size();i++){
            enemyUnitsPower = enemyUnitsPower + neighborUnits.get(i).meleeUnits + neighborUnits.get(i).rangeUnits + neighborUnits.get(i).specialUnits*3;
        }
        thisUnitPower += thisUnit.meleeUnits + thisUnit.rangeUnits + thisUnit.specialUnits*3;
        if((thisUnitPower<enemyUnitsPower*0.85)) {
            maxMelee = maxAbleToBuy(10,3);
            maxRange = maxAbleToBuy(15,2);
            maxSpecial = maxAbleToBuy(30,8);
            if(maxMelee >=maxRange && maxMelee >=maxSpecial*4){
                if(recalcPower(enemyUnitsPower, thisUnitPower, maxMelee, 0, 0))
                {
                    calculation += 1;
                }
                } else if(maxSpecial*4>=maxRange && maxSpecial*4>=maxMelee )
                {
                    if(recalcPower(enemyUnitsPower, thisUnitPower, 0, 0, maxSpecial))
                    {
                        calculation += 1;
                    }
                } else {
                    if(recalcPower(enemyUnitsPower, thisUnitPower, 0, maxRange, 0))
                    {
                        calculation += 1;
                    }
                }
        } else calculation -= 0.3;
       
       return calculation;
    }
    
    /**
     * Przeliczenie siły oddziału.
     * @param enemyPower Siła wrogich jednostek.
     * @param allyPower Siła jednostek gracza.
     * @param melee Ilość jednostek bliskiego zasięgu.
     * @param range Ilość jednostek dalekiego zasięgu.
     * @param special Ilość jednostek specjalnych.
     * @return True, jeżeli siła jednostek gracza jest wystarczająca do ataku; False, jeżeli siła tych jednostek nie jest wystarczająca.
     */
 private boolean recalcPower(int enemyPower, int allyPower, int melee, int range, int special){
    allyPower+= melee+range+special*3;
    
    return allyPower >= enemyPower*0.85;
 }
 
 /**
  * Znalezienie maksymalnej liczby jednostek możliwych do zakupienia.
  * @param goldCost Złoto.
  * @param foodCost Jedzenie.
  * @return Maksymalna ilość jednostek.
  */
 private int getMax(int goldCost, int foodCost){
     int maxGold = (int)(this.gold/goldCost);
     int maxFood = (int)(this.food/foodCost);
     if(maxGold<=maxFood) return maxGold; else return maxFood;
 }
 
 /**
  * Pobieranie ilości jednostek jakie można zakupić.
  * @param goldCost Złoto.
  * @param foodCost Jedzenie.
  * @return Maksymalna liczba jednostek.
  */
 private int maxAbleToBuy(int goldCost, int foodCost){
    int max = 0;
    max = getMax(goldCost,foodCost);
    return max;
 }
 
    /**
     * Pobierz pola sąsiednie dla jednostki.
     * @param thisUnit Jednostka.
     * @param mapX Szerokość mapy.
     * @param mapY Wysokość mapy.
     */
    public void getNeighbors(Units thisUnit, int mapX, int mapY){
        pHex = new Hex(thisUnit.q,thisUnit.r,thisUnit.s);
        hexNeighbors.clear();
        neighborUnits.clear();
        for(int i=0; i<6;i++)
        {
            if(tMS.containCubeArr(Hex.neighbor(pHex, i).q,Hex.neighbor(pHex, i).r,Hex.neighbor(pHex, i).s) == true){
                hexNeighbors.add(Hex.neighbor(pHex, i));
            }
        }
    }
    
    /**
     * Pobierz dzienne dostawy złota.
     * @param color Kolor gracza.
     * @param buildingsList Lista budynków.
     * @return Liczba dziennego złota.
     */
    public int getSummaryGold(ArrayList<TileBuildings> buildingsList, String color){
        int goldSummary = 0;
        for(int i=0;i<buildingsList.size();i++){
            if(buildingsList.get(i).playerColor.equals(new String(color)))
            {
                goldSummary += buildingsList.get(i).dayGold;
            }
        }
        
        return goldSummary;
    }

    /**
     * Pobierz dzienne dostawy jedzenia.
     * @param buildingsList Lista budynków.
     * @param color Kolor gracza.
     * @return Liczba dziennego jedzenia.
     */
    public int getSummaryFood(ArrayList<TileBuildings> buildingsList, String color){
        int foodSummary = 0;
        for(int i=0;i<buildingsList.size();i++){
            if(buildingsList.get(i).playerColor.equals(new String(color)))
            {
                foodSummary += buildingsList.get(i).dayFood;
            }
        }
        
        return foodSummary;
    }
    
    /**
     * Obliczenia dla zakupu budynku.
     * @param thisUnit Jednostka.
     * @param buildingsList Lista budynków.
     * @return Obliczenia dla zakupu budynku.
     */
    public double buyBuilding(Units thisUnit, ArrayList<TileBuildings> buildingsList){
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
