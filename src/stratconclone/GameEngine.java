/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import static java.lang.System.exit;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.FileHandler;

/**
 *
 * @author matthew
 */
public class GameEngine {

    public enum GameState {

        INIT, WAITINGFORUSER, INPROGRESS, ENDED
    }
    private GameState gameState;
    private int dayNumber;
    private int currentPlayerId;
    private boolean currentlyProcessingPlayerTurn;
    private int selectedUnitListId;
    private int selectedCityListId;
    private Player player1;
    private Player player2;
//    private Grid grid;
    private final static Logger logger = Logger.getLogger(GameEngine.class.getName());
    //private boolean idle;
    //private int command; // human player command e.g. (1) wake or (2) move stack
    private GUI gui;

    //getters & setters
    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        System.out.println("game state = " + gameState);
        this.gameState = gameState;
    }

    public boolean getGameOver() {
        if (this.gameState == GameState.ENDED) {
            return true;
        } else {
            return false;
        }
    }

    public int getDayNumber() {
        return this.dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getCurrentPlayerId() {
        return this.currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public boolean getCurrentlyProcessingPlayerTurn() {
        return this.currentlyProcessingPlayerTurn;
    }

    public void setCurrentlyProcessingPlayerTurn(boolean currentlyProcessingPlayerTurn) {
        this.currentlyProcessingPlayerTurn = currentlyProcessingPlayerTurn;
    }

    public int getSelectedUnitListId() {
        return this.selectedUnitListId;
    }

    public void setSelectedUnitListId(int selectedUnitListId) {
        this.selectedUnitListId = selectedUnitListId;
    }

    public int getSelectedCityListId() {
        return this.selectedCityListId;
    }

    public void setSelectedCityListId(int selectedCityListId) {
        this.selectedCityListId = selectedCityListId;
    }

    public boolean getIsVisiblePlayer1() {
        return player1.getIsVisible();
    }

    public boolean getIsVisiblePlayer2() {
        return player2.getIsVisible();
    }

    public void setGui(GUI _gui) {
        gui = _gui;
    }

    public void init(int dayNumber, int playerId) throws java.io.IOException {

        //logger.addHandler(new ConsoleHandler());
//        try {
//            FileHandler handler = new FileHandler("myapp-log.%u.%g.txt", true);
//        } catch (java.io.IOException ex) {
//            throw ex;
//        }
        //logger.setLevel(Level.INFO);
        //logger.setLevel(Level.ALL);
        //logger.entering(getClass().getName(), "init");
        setGameState(GameState.INIT);
        setDayNumber(dayNumber);
        setCurrentPlayerId(playerId);
        setCurrentlyProcessingPlayerTurn(false);
        setSelectedUnitListId(-1);
        setSelectedCityListId(-1);

        setupUnitRefs();

        player1 = new Player();
        player1.setPlayerId(1);
        player1.setIsAI(true);
        player1.setIsVisible(true); // for debugging

        player2 = new Player();
        player2.setPlayerId(2);
        player2.setIsAI(true); // if Player2 is HUMAN set this to false
        player2.setIsVisible(true);

//        //grid = new Grid();
//        grid.init();
        //grid.display();
        //IslandList.display();
        setGameState(GameState.INPROGRESS);

        //logger.exiting(getClass().getName(), "init");
    }

    public boolean assignPlayerStartCity(int playerId, Grid grid) {

        boolean isPortCity = false;

        System.out.println("in assignPlayerStartCity(" + playerId + ")");

        try {
            // identify city at random
            //Cell cell = CityList.getRandomEmpty();
            City city = CityList.getRandomEmpty();

            city.setPlayerId(playerId);

            System.out.println("city at (" + city.getRow() + ", " + city.getCol() + ")");

            isPortCity = city.isPort(grid);

            // clear fog of war for player
            grid.clearFogOfWar(playerId, city.getRow(), city.getCol());

            // set city production (if player is HUMAN they should be prompted. If player is computer use AI)
            switch (playerId) {
                case 1:
                    // this assumes player1 is always AI 
                    city.setNextUnitProductionAI();
                    break;
                case 2:
                    if (player2.getIsAI()) {
                        city.setNextUnitProductionAI();
                    } else {
                        //System.out.println("in ge assignPlayerStartCity about to show select city production dialogue");
                        // prompt HUMAN player to set initial production unit type
                        //setGameState(GameState.WAITINGFORUSER);
                        //gui.buildDialogueCityProduction(isPortCity);
                        //gui.showDialogueCityProduction();
                    }
                    break;
            }

        } catch (Exception ex) {
            exit(-1);
        }

        return isPortCity;
    }

    private void setupUnitRefs() {

        UnitRef[] unitRef = new UnitRef[7];

        unitRef[0] = new UnitRef();
        unitRef[0].setUnitType(UnitRef.UnitType.TANK);
        unitRef[0].setUnitName("Tank");
        unitRef[0].setDaysToProduce(4);
        unitRef[0].setStrength(2);
        unitRef[0].setAttackRange(1);
        unitRef[0].setCaputuresCity(true);
        unitRef[0].setMovesOnLand(true);
        unitRef[0].setMovesOnWater(false);
        unitRef[0].setMovesPerTurn(2);
        unitRef[0].setMaxFuel(-1);

        unitRef[1] = new UnitRef();
        unitRef[1].setUnitType(UnitRef.UnitType.FIGHTER);
        unitRef[1].setUnitName("Fighter");
        unitRef[1].setDaysToProduce(6);
        unitRef[1].setStrength(1);
        unitRef[1].setAttackRange(1);
        unitRef[1].setCaputuresCity(false);
        unitRef[1].setMovesOnLand(true);
        unitRef[1].setMovesOnWater(true);
        unitRef[1].setMovesPerTurn(20);
        unitRef[1].setMaxFuel(20);

        unitRef[2] = new UnitRef();
        unitRef[2].setUnitType(UnitRef.UnitType.BATTLESHIP);
        unitRef[2].setUnitName("Battleship");
        unitRef[2].setDaysToProduce(20);
        unitRef[2].setStrength(18);
        unitRef[2].setAttackRange(4);
        unitRef[2].setCaputuresCity(false);
        unitRef[2].setMovesOnLand(false);
        unitRef[2].setMovesOnWater(true);
        unitRef[2].setMovesPerTurn(3);
        unitRef[2].setMaxFuel(-1);

        unitRef[3] = new UnitRef();
        unitRef[3].setUnitType(UnitRef.UnitType.BOMBER);
        unitRef[3].setUnitName("Bomber");
        unitRef[3].setDaysToProduce(25);
        unitRef[3].setStrength(1);
        unitRef[3].setAttackRange(1);
        unitRef[3].setCaputuresCity(false);
        unitRef[3].setMovesOnLand(true);
        unitRef[3].setMovesOnWater(true);
        unitRef[3].setMovesPerTurn(10);
        unitRef[3].setMaxFuel(30);

        unitRef[4] = new UnitRef();
        unitRef[4].setUnitType(UnitRef.UnitType.CARRIER);
        unitRef[4].setUnitName("Carrier");
        unitRef[4].setDaysToProduce(10);
        unitRef[4].setStrength(12);
        unitRef[4].setAttackRange(1);
        unitRef[4].setCaputuresCity(false);
        unitRef[4].setMovesOnLand(false);
        unitRef[4].setMovesOnWater(true);
        unitRef[4].setMovesPerTurn(3);
        unitRef[4].setMaxFuel(-1);

        unitRef[5] = new UnitRef();
        unitRef[5].setUnitType(UnitRef.UnitType.DESTROYER);
        unitRef[5].setUnitName("Destroyer");
        unitRef[5].setDaysToProduce(8);
        unitRef[5].setStrength(3);
        unitRef[5].setAttackRange(1);
        unitRef[5].setCaputuresCity(false);
        unitRef[5].setMovesOnLand(false);
        unitRef[5].setMovesOnWater(true);
        unitRef[5].setMovesPerTurn(4);
        unitRef[5].setMaxFuel(-1);

        unitRef[6] = new UnitRef();
        unitRef[6].setUnitType(UnitRef.UnitType.TRANSPORT);
        unitRef[6].setUnitName("Transport");
        unitRef[6].setDaysToProduce(8);
        unitRef[6].setStrength(3);
        unitRef[6].setAttackRange(1);
        unitRef[6].setCaputuresCity(false);
        unitRef[6].setMovesOnLand(false);
        unitRef[6].setMovesOnWater(true);
        unitRef[6].setMovesPerTurn(3);
        unitRef[6].setMaxFuel(-1);

        unitRef[6].setUnitType(UnitRef.UnitType.SUBMARINE);
        unitRef[6].setUnitName("Submarine");
        unitRef[6].setDaysToProduce(8);
        unitRef[6].setStrength(3);
        unitRef[6].setAttackRange(1);
        unitRef[6].setCaputuresCity(false);
        unitRef[6].setMovesOnLand(false);
        unitRef[6].setMovesOnWater(true);
        unitRef[6].setMovesPerTurn(3);
        unitRef[6].setMaxFuel(-1);

        // UnitTypeId, UnitName, daysToProduce, strength, attackRange, caputuresCity, movesOnLand, movesOnWater, movesPerTurn, maxFuel
        //oUnitRef[8] = new UnitRef(8, "Artillery", 4, 1, 4, true, true, false, 1, -1);
        //unitRef[9] = new UnitRef(9, "Helicopter", 8, 1, 1, false, true, true, 10, 10);
    }

    public void doPlayerMove(Grid grid) {

        //logger.entering(getClass().getName(), "doPlayerMove");
        switch (getGameState()) {
            case INIT:
                break;
            case WAITINGFORUSER:
                break;
            case INPROGRESS:
                /*
                 // just for testing
                 System.out.println("in ge doPlayerMove  case INPROGRESS");
                 if (getCurrentlyProcessingPlayerTurn() == false) {
                 gui.showDialogueAiSurrender();
                 setGameState(GameState.WAITINGFORUSER);
                 }
                 */
                processPlayerTurn(grid);
                break;
            case ENDED:
                break;
        }
    }

    private void processPlayerTurn(Grid grid) {

        if (getCurrentlyProcessingPlayerTurn() == false) {

            setCurrentlyProcessingPlayerTurn(true);

            // player1 moves first
            if (UnitList.hasUnitsWithMovesLeftToday(1)) {

                //logger.fine("GameEngine.doPlayerMove() player1 has units with moves left today");
                setCurrentPlayerId(1);

                int unitListId = UnitList.getNextUnitWithMovesLeftToday(1);
                setSelectedUnitListId(unitListId);
                UnitList.highlight(unitListId);

                UnitList.moveAI(unitListId, grid);
                

            } else if (UnitList.hasUnitsWithMovesLeftToday(2)) {

                //logger.fine("GameEngine.doPlayerMove() player2 has units with moves left today");
                setCurrentPlayerId(2);

                int unitListId = UnitList.getNextUnitWithMovesLeftToday(2);
                setSelectedUnitListId(unitListId);
                UnitList.highlight(unitListId);

                if (player2.getIsAI()) {
                    UnitList.moveAI(unitListId, grid);
                }

            } else {

                if (player1.getIsAI()) {
                    EndPlayerTurn();
                }

                if (player2.getIsAI()) {
                    EndPlayerTurn();
                } else {
                    // if player 2 (human) has no units, they should not need to click button to End Turn
                    if (UnitList.getCount(2) == 0) {
                        EndPlayerTurn();
                    } else {
                        // player2 is human, were are waiting for user to click End Turn button
                        System.out.println("system waiting for player 2 to click button End Turn");
                        setGameState(GameState.WAITINGFORUSER);
                        gui.setEndTurnButtonEnabled(true);
                    }
                }

            }

            // TODO
            //CityList.updateSelectedCityPanelInformation(getSelectedCityListId());
        }

        setCurrentPlayerId(-1);
        setCurrentlyProcessingPlayerTurn(false);

        //logger.exiting(getClass().getName(), "doPlayerMove");
    }

    public void EndPlayerTurn() {

        gui.setEndTurnButtonEnabled(false);
        
        // is there a winner?
        int unitCountPlayer1 = UnitList.getCount(1);
        int unitCountPlayer2 = UnitList.getCount(2);
        int cityCountPlayer1 = CityList.getCount(1);
        int cityCountPlayer2 = CityList.getCount(2);

        checkforGameOver(unitCountPlayer1, unitCountPlayer2, cityCountPlayer1, cityCountPlayer2);

        // player1 and player2 have no units to be moved, progress to next day
        this.nextDay();
    }

    private void nextDay() {

        //logger.entering(getClass().getName(), "nextDay");
        setDayNumber(getDayNumber() + 1);

        System.out.println("Day " + getDayNumber());

        UnitList.resetMovesLeftToday();
        UnitList.resetAttacksLeftToday();
        CityList.manageProduction();

        //logger.exiting(getClass().getName(), "nextDay");
    }

    void checkforGameOver(int unitCountPlayer1, int unitCountPlayer2, int cityCountPlayer1, int cityCountPlayer2) {

        //logger.entering(getClass().getName(), "checkforGameOver");
        int minDayNumber = 10;
        int minUnitNumber = 10;
        int minCityNumber = 0;

        if (getDayNumber() >= minDayNumber) {

            //should player 1 surrender?
            if (unitCountPlayer1 <= minUnitNumber && cityCountPlayer1 <= minCityNumber) {

                if (player1.getIsAI()) {
                    gameOverPlayAgain(2); // player 2 wins
                } else {
                    // player1 is HUMAN so show Dialogue Surrender
                    // TODO
                    // oDialogueSurrender.show();
                    // if player1 (HUMAN) surrenders...
                    gameOverPlayAgain(2); // player 2 wins
                }

            } else if (unitCountPlayer2 <= minUnitNumber && cityCountPlayer2 <= minCityNumber) {

                if (player2.getIsAI()) {
                    gameOverPlayAgain(1); // player1 wins
                } else {
                    // player2 is HUMAN so show Dialogue Surrender
                    // TODO
                    // oDialogueSurrender.show();
                    // if player2 (HUMAN) surrenders...
                    gameOverPlayAgain(1); // player 1 wins
                }

            }

        }

        //logger.exiting(getClass().getName(), "checkforGameOver");
    }

    public void userActionPlayerAcceptedSurrenderYes() {

        System.out.println("in ge userActionPlayerAcceptedSurrenderYes()");
        if (getCurrentPlayerId() == 1) {
            gameOverPlayAgain(2);
        } else {
            gameOverPlayAgain(1);
        }
    }

    public void userActionPlayerAcceptedSurrenderNo() {

        System.out.println("in ge userActionPlayerAcceptedSurrenderNo()");
        setGameState(GameState.INPROGRESS);
    }

    private void gameOverPlayAgain(int winningPlayerId) {

        System.out.println("in ge gameOver(" + winningPlayerId + ")");
        setGameState(GameState.WAITINGFORUSER);
        if (player1.getIsAI() && winningPlayerId == 2) {
            System.out.println("");
            System.out.println("General, I surrender.");
            System.out.println("");
            setGameState(GameState.WAITINGFORUSER);
            gui.showDialogueAiSurrender();
        } else {
            gui.showDialogueGameOverPlayAgain();
        }

    }

    public void userActionGameOverPlayAgainYes(Grid grid) {

        System.out.println("in ge userActionGameOverPlayAgainYes()");

        //setGameState(GameState.INIT);
        try {
            // TODO
            //init(0, -1);
            System.out.println("feature not yet implemented: play again");
            System.exit(0);

        } catch (Exception ex) {
        }

        //setGameState(GameState.INPROGRESS);
    }

    public void userActionGameOverPlayAgainNo() {

        System.out.println("in ge userActionGameOverPlayAgainNo()");
        setGameState(GameState.WAITINGFORUSER);
        if (getCurrentPlayerId() == 1) {
            displayThankYouForPlayingDialogue(2);
        } else {
            displayThankYouForPlayingDialogue(1);
        }
    }

    private void displayThankYouForPlayingDialogue(int winningPlayerId) {

        System.out.println("in ge displayThankYouForPlayingDialogue()");
        if (winningPlayerId != -1) {
            System.out.println("Player " + winningPlayerId + " has won!");
        }
        System.out.println("Thank you for playing StratConClone!");
        System.out.println("");

        setGameState(GameState.WAITINGFORUSER);
        gui.showDialogueThanksForPlaying();

        //logger.info("Game Over");
    }

    public void userActionQuit() {

        System.out.println("");
        System.out.println("Bye.");
        System.out.println("");
        gui.quit();
        System.exit(0);
    }


    public void mouseClicked(int screenX, int screenY) {
        System.out.println("in ge mouseClicked("+screenX+","+screenY+")");
    }
        
    public void mousePressed(int screenX, int screenY) {
        System.out.println("in ge mousePressed("+screenX+","+screenY+")");
    }
    
    public void mouseDragged(int screenX, int screenY) {
        System.out.println("in ge mouseDragged("+screenX+","+screenY+")");
    }
}
