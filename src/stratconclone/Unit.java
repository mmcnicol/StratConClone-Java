/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

//import java.util.ArrayList;
/**
 *
 * @author matthew
 */
public class Unit {

    public enum Role {

        EXPLORE, DEFEND, PATROL, TRANSPORTABLE, ASLEEP
    }

    public enum Task {

        NONE, MOVETO, MOVETOTRANSPORT, INTRANSPORT
    }
    private UnitRef.UnitType unitType;
    private int playerId;
    private int row, col;
    private int moveToRow, moveToCol;
    private int dayBuilt;
    private Role role;
    private Task task;

    private String unitName;
    private int strength;
    private int attackRange;
    private boolean caputuresCity;
    private boolean movesOnLand;
    private boolean movesOnWater;
    private int movesPerTurn;
    private int movesLeftToday;
    private int attacksLeftToday;
    private int fuel; // -1 = N/A
    private int bombBlastRadius; // -1 = N/A

//    //ArrayList listCargo;
//    //int thisUnitIsCargoOfUnitId; // -1 = N/A
//    private final int[] cargoUnitListId = new int[6];
//    //float[] coswave = new float[width];
//    private int isCargoOfUnitListId;
    public UnitRef.UnitType getUnitType() {
        return this.unitType;
    }

    public void setUnitType(UnitRef.UnitType unitType) {
        this.unitType = unitType;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
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

    public int getMoveToRow() {
        return this.moveToRow;
    }

    public void setMoveToRow(int row) {
        this.moveToRow = row;
    }

    public int getMoveToCol() {
        return this.moveToCol;
    }

    public void setMoveToCol(int col) {
        this.moveToCol = col;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public int getMovesPerTurn() {
        return this.movesPerTurn;
    }

    public void setMovesPerTurn(int movesPerTurn) {
        this.movesPerTurn = movesPerTurn;
    }

    public int getMovesLeftToday() {
        return this.movesLeftToday;
    }

    public void setMovesLeftToday(int movesLeftToday) {
        this.movesLeftToday = movesLeftToday;
    }

    
    public int getAttacksLeftToday() {
        return this.attacksLeftToday;
    }

    public void setAttacksLeftToday(int attacksLeftToday) {
        this.attacksLeftToday = attacksLeftToday;
    }

    public void resetAttacksLeftToday() {
        this.attacksLeftToday = 2;
    }
        
    public boolean isAt(Cell cell) {
        
        boolean result = false;
        
        if (getRow()==cell.getRow() && getCol()==cell.getCol()) result = true;
        
        return result;
    }
    
    public void resetMovesLeftToday() {
        this.movesLeftToday = this.movesPerTurn;
    }

    public void moveAI(Grid grid) {

    }
    


    //abstract void identifyNextMoveTypeSpecificAI(int unitListId, Grid grid);
    public void updateMoveTo(Cell moveTo) {

        // if unit has reached destination
        if (getMoveToRow() == moveTo.getRow() && getMoveToCol() == moveTo.getCol()) {
            setMoveToRow(-1);
            setMoveToCol(-1);
        } else {
            // record where instructed to move to
            setMoveToRow(moveTo.getRow());
            setMoveToCol(moveTo.getCol());
        }

        //grid.clearFogOfWar(getPlayerId(), moveTo.getRow(), moveTo.getCol());
        //System.out.println("unit moved to " + moveTo.getRow() + "," + moveTo.getCol());
    }

    protected Cell getNextStepMoveTowards(Cell location) {
        Cell nextStep = new Cell();

        // simple move logic // TODO change to path finding algorithm
        if (location.getRow() < getRow()) {
            nextStep.setRow(getRow() - 1);
        } else if (location.getRow() > getRow()) {
            nextStep.setRow(getRow() + 1);
        } else {
            nextStep.setRow(getRow());
        }

        if (location.getCol() < getCol()) {
            nextStep.setCol(getCol() - 1);
        } else if (location.getCol() > getCol()) {
            nextStep.setCol(getCol() + 1);
        } else {
            nextStep.setCol(getCol());
        }

        return nextStep;
    }

    public boolean getAttackOutcome() {
        
        boolean result = false;
        
        // decide attack outcome // TODO take into account unit strength
        if ((int) Util.random(1, 1000) % 2 == 0) result = true;
        
        return result;
    }
}
