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
public class Cell {

    public enum Type {

        LAND, SEA
    }

    private int row, col, islandId;
    private Type type;
    private boolean fogOfWarPlayer1, fogOfWarPlayer2;

    //constants
    //private final int width=16;
    //private final int height=16;
    //getters & setters
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

    public int getIslandId() {
        return this.islandId;
    }

    public void setIslandId(int islandId) {
        this.islandId = islandId;
    }

    public boolean isLand() {
        if (this.type == Type.LAND) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isSea() {
        if (this.type == Type.SEA) {
            return true;
        } else {
            return false;
        }
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean getFogOfWar(int playerId) {
        if (playerId == 1) {
            return this.fogOfWarPlayer1;
        } else {
            return this.fogOfWarPlayer2;
        }
    }

    public void setFogOfWar(int playerId, boolean fogOfWar) {
        if (playerId == 1) {
            this.fogOfWarPlayer1 = fogOfWar;
        } else {
            this.fogOfWarPlayer2 = fogOfWar;
        }
    }

}
