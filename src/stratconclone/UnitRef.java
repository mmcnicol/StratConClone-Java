/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

/**
 *
 * @author matthew
 */
public class UnitRef {

    public static enum UnitType {

        TANK, FIGHTER, BATTLESHIP, BOMBER, CARRIER, DESTROYER, TRANSPORT, SUBMARINE
    }
    private UnitType unitType;
    private String unitName;
    private int daysToProduce;
    private int strength;
    private int attackRange;
    private boolean caputuresCity;
    private boolean movesOnLand;
    private boolean movesOnWater;
    private int movesPerTurn;
    private int maxFuel; // -1 = N/A

    /*
     cUnitRef(int intUnitTypeId_, string strUnitName_, int daysToProduce_, int strength_, int attackRange_, bool caputuresCity_, bool movesOnLand_, bool movesOnWater_, int movesPerTurn_, int maxFuel_) {
     intUnitTypeId = intUnitTypeId_;
     strUnitName = strUnitName_;
     daysToProduce = daysToProduce_;
     strength = strength_;
     attackRange = attackRange_;
     caputuresCity = caputuresCity_;
     movesOnLand = movesOnLand_;
     movesOnWater = movesOnWater_;
     movesPerTurn = movesPerTurn_;
     maxFuel = maxFuel_;
     }
     */

    /*
     void print() {
     println("unitTypeId=" + intUnitTypeId
     + ", unitName=" + strUnitName
     + ", daysToProduce=" + daysToProduce
     + ", strength=" + strength
     + ", attackRange=" + attackRange
     + ", caputuresCity=" + caputuresCity
     + ", movesOnLand=" + movesOnLand
     + ", movesOnWater=" + movesOnWater
     + ", movesPerTurn=" + movesPerTurn
     + ", maxFuel=" + maxFuel
     );
     }
     */
    public UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getDaysToProduce() {
        return this.daysToProduce;
    }

    public void setDaysToProduce(int daysToProduce) {
        this.daysToProduce = daysToProduce;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAttackRange() {
        return this.attackRange;
    }

    public void setAttackRange(int attackRange) {
        this.attackRange = attackRange;
    }

    public boolean getCaputuresCity() {
        return this.caputuresCity;
    }

    public void setCaputuresCity(boolean caputuresCity) {
        this.caputuresCity = caputuresCity;
    }

    public boolean getMovesOnLand() {
        return this.movesOnLand;
    }

    public void setMovesOnLand(boolean movesOnLand) {
        this.movesOnLand = movesOnLand;
    }

    public boolean getMovesOnWater() {
        return this.movesOnWater;
    }

    public void setMovesOnWater(boolean movesOnWater) {
        this.movesOnWater = movesOnWater;
    }

    public int getMovesPerTurn() {
        return this.movesPerTurn;
    }

    public void setMovesPerTurn(int movesPerTurn) {
        this.movesPerTurn = movesPerTurn;
    }

    public int getMaxFuel() {
        return maxFuel;
    }

    public void setMaxFuel(int maxFuel) {
        this.maxFuel = maxFuel;
    }

    public boolean canFly() {
        if (this.maxFuel == -1) {
            return false;
        } else {
            return true;
        }
    }

    public int calculateBombBlastRadius(int gameDayNum) {

        /*
         Initially, Bombers have a radius of 0, and can thus only destroy one square. However, for Bombers started after day 50, the radius of the Bomber goes up to 1, meaning that the same bomb can now destroy 9 squares and everything in them. After day 100, Bombers will have radius 2, and will destroy a total of 25 squares. 
         */
        int result = 0;
        int targetDayNum = 0;

        targetDayNum = targetDayNum + 20 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        targetDayNum = targetDayNum + 5 + 50;
        if (gameDayNum > targetDayNum) {
            result++;
        }

        return result;
    }
}
