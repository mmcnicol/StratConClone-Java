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
public class Player {

    private int playerId;
    private boolean isAI;
    private boolean isVisible;

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public void setIsAI(boolean isAI) {
        this.isAI = isAI;
    }

    public boolean getIsAI() {
        return this.isAI;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean getIsVisible() {
        return this.isVisible;
    }

}
