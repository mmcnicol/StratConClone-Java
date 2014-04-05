/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.max;

/**
 *
 * @author matthew
 */
public class City {

    private int playerId;
    private int islandId;
    private int row, col;
    private int strength;

    private UnitRef.UnitType productionUnitType;
    //private String productionUnitTypeName;
    private int productionDaysLeft;

    //getters & setters
    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getIslandId() {
        return this.islandId;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    public int getRow() {
        return this.row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return this.col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getStrength() {
        return this.strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public boolean isNearby(int row, int col) {
        if (abs(row - this.row) <= 2 && abs(col - this.col) <= 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isAt(Cell location) {
        if (this.row == location.getRow() && this.col == location.getCol()) {
            return true;
        } else {
            return false;
        }
    }
    
    /*
    public boolean isAt(int row, int col) {
        if (this.row == row && this.col == col) {
            return true;
        } else {
            return false;
        }
    }
    */

    public boolean isOccupied() {
        if (this.playerId == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isPort(Grid grid) {

        boolean result = false;

        for (int r = max(0, this.row - 1); r <= min(99, this.row + 1); r = r + 1) {
            for (int c = max(0, this.col - 1); c <= min(99, this.col + 1); c = c + 1) {
                if (grid.isSea(r, c)) {
                    result = true;
                }
            }
        }

        return result;
    }

    public boolean isWithin(int numCells, int row, int col) {

        if ((this.row >= (row - numCells) && this.row <= (row + numCells))
                && (this.col >= (col - numCells) && this.col <= (col + numCells))) {
            return true;
        } else {
            return false;
        }
    }

    public UnitRef.UnitType getProductionUnitType() {
        return this.productionUnitType;
    }

    public void setProductionUnitType(UnitRef.UnitType productionUnitType) {
        this.productionUnitType = productionUnitType;
    }

    public int getProductionDaysLeft() {
        return this.productionDaysLeft;
    }

    public void setProductionDaysLeft(int productionDaysLeft) {
        this.productionDaysLeft = productionDaysLeft;
    }

    public void manageProduction() {

        if (this.isOccupied()) {

            if (this.getProductionDaysLeft() > 1) {
                this.setProductionDaysLeft(this.getProductionDaysLeft() - 1);

            } else {

                // city has finished producing a unit
                UnitList.add(this.getPlayerId(), this.getProductionUnitType(), this.getRow(), this.getCol());

                // start building next unit
                this.setNextUnitProductionAI(); // for testing purposes

                /*
                 if (intPlayerId == 2) {

                 doNextUnitProductionAI(intPlayerId);

                 } else if (intPlayerId == 1 && oPlayer1.getIsAI()) {

                 if (intPlayerId == 1) {
                 oViewport.scrollIfAppropriate(intCellX, intCellY);
                 }
                 doNextUnitProductionAI(intPlayerId);

                 } else if (intPlayerId == 1) {

                 if (intPlayerId == 1) {
                 oViewport.scrollIfAppropriate(intCellX, intCellY);
                 }

                 if (productionUnitTypeId != -1) {

                 productionUnitTypeId = oUnitRef[productionUnitTypeId].getUnitTypeId();
                 productionDaysLeft = oUnitRef[productionUnitTypeId].getDaysToProduce();
                 } else {

                 productionUnitTypeId = oUnitRef[0].getUnitTypeId();
                 productionDaysLeft = oUnitRef[0].getDaysToProduce();
                 }
                
                 //                     println("in city.manageProduction() player="+intPlayerId+" display city production dialogue...");
                 //                     oCityList.updateSelectedCityPanelInformation(iCityListId_);
                 //                     oGameEngine.setSelectedCityListId(iCityListId_);
                 //                     oDialogueCityProduction.display();

                 }
                 */
            }
        } else {
            //println("in cCity.manageProduction() cityEmpty=true");
        }
    }

    public void setNextUnitProductionAI() {
        // TODO - make this more intelligent
        this.setProductionUnitType(UnitRef.UnitType.TANK);
        this.setProductionDaysLeft(4);
    }

    private void doNextUnitProductionAI(int iPlayerId_) {

        /* ***************************************** 
         // version 1:
         int numTanks=0;
         int numFighters=0;
         int numDestroyer=0'
         int numTransport=0;
         int numSubmarine=0;
		
         numTank = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 0);
         numFighter = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 1);
         numDestroyer = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 5);
         numTransport = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 6);
         numSubmarine = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 7);
		
         if ( numTank < 5 ) {
         productionUnitTypeId= oUnitRef[0].getUnitTypeId(); 
         productionDaysLeft = oUnitRef[0].getDaysToProduce();
			
         } else if ( numFighter < 5 ) {
         productionUnitTypeId= oUnitRef[1].getUnitTypeId(); 
         productionDaysLeft = oUnitRef[1].getDaysToProduce();
			
         } else if ( isPort() && numDestroyer < 2 ) {
         productionUnitTypeId= oUnitRef[5].getUnitTypeId(); 
         productionDaysLeft = oUnitRef[5].getDaysToProduce();
			
         } else if ( isPort() && numTransport < 2 ) {
         productionUnitTypeId= oUnitRef[6].getUnitTypeId(); 
         productionDaysLeft = oUnitRef[6].getDaysToProduce();
			
         } else if ( isPort() && numSubmarine < 2 ) {
         productionUnitTypeId= oUnitRef[7].getUnitTypeId(); 
         productionDaysLeft = oUnitRef[7].getDaysToProduce();
			
         } else {
         productionUnitTypeId= oUnitRef[0].getUnitTypeId(); 
         productionDaysLeft = oUnitRef[0].getDaysToProduce();		
         }
         */
        /* ***************************************** */
		// version 2:
        /*
         numTank = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 0);
         numTransport = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 6);
         numDestroyer = oUnitList.getCountOfPlayerUnitsByUnitType(iPlayerId_, 5);

         //player needs a minimum number of tanks
         //if (numTank<=2 || oCityList.getCountPlayerCityProducingUnit(intPlayerId, 0)<=2 ) {
         if (numTank <= 6) {

         productionUnitTypeId = oUnitRef[0].getUnitTypeId();
         productionDaysLeft = oUnitRef[0].getDaysToProduce();

         //player should have a minimum number of transports
         //} else if (isPort() && ( numTransport <= 1 && oCityList.getCountPlayerCityProducingUnit(intPlayerId, 6)<=1 ) ) {
         } else if (isPort() && numTransport <= 1) {

         productionUnitTypeId = oUnitRef[6].getUnitTypeId();
         productionDaysLeft = oUnitRef[6].getDaysToProduce();

         //player should have a minimum number of destroyers
         //} else if ( isPort() && ( numDestroyer <= 1  && oCityList.getCountPlayerCityProducingUnit(intPlayerId, 5)<=1 ) ) {
         } else if (isPort() && numDestroyer <= 1) {

         productionUnitTypeId = oUnitRef[5].getUnitTypeId();
         productionDaysLeft = oUnitRef[5].getDaysToProduce();

         // else if port city, build random unit
         } else if (isPort()) {
         switch ((int) random(1, 8)) {
         case 1:
         productionUnitTypeId = oUnitRef[4].getUnitTypeId(); // carrier
         productionDaysLeft = oUnitRef[4].getDaysToProduce();
         break;
         case 2:
         productionUnitTypeId = oUnitRef[1].getUnitTypeId(); // fighter
         productionDaysLeft = oUnitRef[1].getDaysToProduce();
         break;
         case 3:
         productionUnitTypeId = oUnitRef[3].getUnitTypeId(); // bomber
         productionDaysLeft = oUnitRef[3].getDaysToProduce();
         break;
         case 4:
         productionUnitTypeId = oUnitRef[0].getUnitTypeId(); // tank
         productionDaysLeft = oUnitRef[0].getDaysToProduce();
         break;
         case 5:
         productionUnitTypeId = oUnitRef[7].getUnitTypeId(); // submarine
         productionDaysLeft = oUnitRef[7].getDaysToProduce();
         break;
         case 6:
         productionUnitTypeId = oUnitRef[2].getUnitTypeId(); // battleship
         productionDaysLeft = oUnitRef[2].getDaysToProduce();
         break;
         case 7:
         productionUnitTypeId = oUnitRef[0].getUnitTypeId(); // tank
         productionDaysLeft = oUnitRef[0].getDaysToProduce();
         break;
         //productionUnitTypeId= oUnitRef[6].getUnitTypeId(); // transport
         //productionDaysLeft = oUnitRef[6].getDaysToProduce();
         //break;				
         case 8:
         productionUnitTypeId = oUnitRef[0].getUnitTypeId(); // tank
         productionDaysLeft = oUnitRef[0].getDaysToProduce();
         break;
         //productionUnitTypeId= oUnitRef[5].getUnitTypeId(); // destroyer
         //productionDaysLeft = oUnitRef[5].getDaysToProduce();
         //break;						
         }

         // else not a port city, build random unit			
         } else {
         switch ((int) random(1, 4)) {
         case 1:
         productionUnitTypeId = oUnitRef[0].getUnitTypeId(); // tank
         productionDaysLeft = oUnitRef[0].getDaysToProduce();
         break;
         case 2:
         productionUnitTypeId = oUnitRef[1].getUnitTypeId(); // fighter
         productionDaysLeft = oUnitRef[1].getDaysToProduce();
         break;
         case 3:
         productionUnitTypeId = oUnitRef[0].getUnitTypeId(); // tank
         productionDaysLeft = oUnitRef[0].getDaysToProduce();
         break;
         case 4:
         productionUnitTypeId = oUnitRef[3].getUnitTypeId(); // bomber
         productionDaysLeft = oUnitRef[3].getDaysToProduce();
         break;
         }
         }
         */
    }
    
    public boolean getAttackOutcome() {
        
        boolean result = false;
        
        // decide attack outcome // TODO take into account unit strength
        if ((int) Util.random(1, 1000) % 2 == 0) result = true;
        
        return result;
    }
        
}
