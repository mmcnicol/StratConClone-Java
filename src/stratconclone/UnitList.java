/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author matthew
 */
public class UnitList {

    //private static List<Unit> list;
    private static List<Unit> list = new ArrayList<Unit>();

    public static void init() {
        //list = new ArrayList<Unit>();
    }

    public static int getCount(int playerId) {
        //return list.size();
        int count = 0;
        for (Unit unit : list) {
            if (unit.getPlayerId() == playerId) {
                count = count + 1;
            }
        }
        return count;
    }

    public static void add(int playerId, UnitRef.UnitType unitType, int row, int col) {

        //Unit unit = new Unit();
        Unit unit;

        switch (unitType) {
            case TANK:
                unit = new Tank();
                break;

            default:
                unit = new Unit();
                break;
        }

        unit.setPlayerId(playerId);
        unit.setUnitType(unitType);
        unit.setMovesPerTurn(2); // TODO
        unit.setMovesLeftToday(2); // TODO
        unit.setRow(row);
        unit.setCol(col);
        unit.setMoveToRow(-1);
        unit.setMoveToCol(-1);
        unit.setRole(Unit.Role.EXPLORE);
        unit.setTask(Unit.Task.NONE);
        unit.setAttacksLeftToday(2); // TODO this nneds to be set to actual value for specific unit
        // TODO - finished setting unit properties 
        list.add(unit);
    }

    public static void resetMovesLeftToday() {
        for (Unit unit : list) {
            unit.resetMovesLeftToday();
        }
    }

    public static void resetAttacksLeftToday() {
        for (Unit unit : list) {
            unit.resetAttacksLeftToday();
        }
    }

    public static boolean hasUnitsWithMovesLeftToday(int playerId) {
        for (Unit unit : list) {
            if (unit.getPlayerId() == playerId && unit.getMovesLeftToday() > 0) {
                return true;
            }
        }
        return false;
    }

    public static int getNextUnitWithMovesLeftToday(int playerId) {
        int count = 0;
        for (Unit unit : list) {
            if (unit.getPlayerId() == playerId && unit.getMovesLeftToday() > 0) {
                return count;
            }
            count++;
        }
        return -1;
    }

    public static void highlight(int unitListId) {
        // TODO
    }

    public static void moveAI(int unitListId, Grid grid) {
        Unit unit = list.get(unitListId);
        unit.moveAI(grid);
    }

    public static boolean isEnemyUnitAt(int playerId, Cell cell) {
        boolean result = false;
        for (Unit unit : list) {
            if (unit.getRow() == cell.getRow() && unit.getCol() == cell.getCol() && unit.getPlayerId() != playerId) {
                result = true;
            }
        }
        return result;
    }

    /*
     public static boolean isEnemyUnitAt(int playerId, int row, int col) {
     boolean result = false;
     for (Unit unit : list) {
     if (unit.getRow() == row && unit.getCol() == col && unit.getPlayerId() != playerId) {
     result = true;
     }
     }
     return result;
     }
     */
    public static void kill(Cell cell) {
        int indexUnitToDelete = -1;
        boolean identifiedUnitToKill = false;
        for (Unit unit : list) {
            if (identifiedUnitToKill == false && unit.getRow() == cell.getRow() && unit.getCol() == cell.getCol()) {
                
                System.out.println("identified player " + unit.getPlayerId() + " unit to kill at " + cell.getRow() + "," + cell.getCol());
                indexUnitToDelete = list.indexOf(unit);
                identifiedUnitToKill = true;
            }
        }
        if (identifiedUnitToKill) {
            System.out.println("about to remove player unit from list");
            Unit unitKilled = list.remove(indexUnitToDelete);
            System.out.println("player unit removed from list");
        }

        System.out.println("done killing player");
    }

    public static boolean getAttackOutcome(Cell cell) {

        boolean result = false;

        for (Unit unit : list) {
            if (unit.isAt(cell)) {
                result = unit.getAttackOutcome();
            }
        }
        return result;
    }

    public static List<Unit> getList() {
        return list;
    }

}
