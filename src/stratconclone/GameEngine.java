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

        INIT, USERDIALOGUE, INPROGRESS, ENDED
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
    GUI gui;

    //getters & setters
    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
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

        logger.addHandler(new ConsoleHandler());
//        try {
//            FileHandler handler = new FileHandler("myapp-log.%u.%g.txt", true);
//        } catch (java.io.IOException ex) {
//            throw ex;
//        }

        //logger.setLevel(Level.INFO);
        logger.setLevel(Level.ALL);

        logger.entering(getClass().getName(), "init");

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

        logger.exiting(getClass().getName(), "init");
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
                    if (player1.getIsAI()) {
                        city.setNextUnitProductionAI();
                    } else {
                        setGameState(GameState.USERDIALOGUE);
                        // TODO
                        //prompt HUMAN player to set initial production unit type
                    }
                    break;
                case 2:
                    if (player2.getIsAI()) {
                        city.setNextUnitProductionAI();
                    } else {
                        setGameState(GameState.USERDIALOGUE);
                        // TODO
                        //prompt HUMAN player to set initial production unit type
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
    
    
    void doPlayerMove(Grid grid) {

        logger.entering(getClass().getName(), "doPlayerMove");

        if (getGameState() == GameState.INPROGRESS) {

            if (getCurrentlyProcessingPlayerTurn() == false) {

                setCurrentlyProcessingPlayerTurn(true);

                // player1 moves first
                if (UnitList.hasUnitsWithMovesLeftToday(1)) {

                    logger.fine("GameEngine.doPlayerMove() player1 has units with moves left today");
                    setCurrentPlayerId(1);

                    int unitListId = UnitList.getNextUnitWithMovesLeftToday(1);
                    setSelectedUnitListId(unitListId);
                    UnitList.highlight(unitListId);

                    if (player1.getIsAI()) {
                        UnitList.moveAI(unitListId, grid);
                    }

                } else if (UnitList.hasUnitsWithMovesLeftToday(2)) {

                    logger.fine("GameEngine.doPlayerMove() player2 has units with moves left today");
                    setCurrentPlayerId(2);

                    int unitListId = UnitList.getNextUnitWithMovesLeftToday(2);
                    setSelectedUnitListId(unitListId);
                    UnitList.highlight(unitListId);

                    if (player2.getIsAI()) {
                        UnitList.moveAI(unitListId, grid);
                    }

                } else {

                    // if player1 and player2 have no units to be moved, progress to next day
                    this.nextDay();

                    int unitCountPlayer1 = UnitList.getCount(1);
                    int unitCountPlayer2 = UnitList.getCount(2);
                    int cityCountPlayer1 = CityList.getCount(1);
                    int cityCountPlayer2 = CityList.getCount(2);

                    checkforGameOver(unitCountPlayer1, unitCountPlayer2, cityCountPlayer1, cityCountPlayer2);

                }

                // TODO
                //CityList.updateSelectedCityPanelInformation(getSelectedCityListId());
            }

            setCurrentPlayerId(-1);
            setCurrentlyProcessingPlayerTurn(false);
            
//           grid.render(p);
            
//            Graphics g = p.getGraphics();
//            g.drawString("Y", 60, 60);
//            g.dispose();
            
//            Graphics2D g = p.canvasImage.createGraphics();
//        g.setRenderingHints(renderingHints);
//        g.setColor(this.color);
//        g.setStroke(stroke);
//        int n = 0;
//        g.drawLine(point.x, point.y, point.x+n, point.y+n);
//        

            /*
             oPanelMap.show();
             if (debugShowPlayer2Viewport) {
             oPanelMapPlayer2.show();
             }

             //oPanelCityList.show();
             //oPanelIslandList.show(); 
             oPanelPlayer1UnitCounts.show();
             if (debugShowPlayer2Viewport) {
             oPanelPlayer2UnitCounts.show();
             }
             */
        }

        logger.exiting(getClass().getName(), "doPlayerMove");
    }

    void nextDay() {

        //logger.entering(getClass().getName(), "nextDay");

        setDayNumber(getDayNumber() + 1);

        System.out.println("Day " + getDayNumber());

        UnitList.resetMovesLeftToday();
        UnitList.resetAttacksLeftToday();
        CityList.manageProduction();
        //oCityList.Draw();
        //oViewport.draw(); // TODO

        //logger.exiting(getClass().getName(), "nextDay");
    }

    void checkforGameOver(int unitCountPlayer1, int unitCountPlayer2, int cityCountPlayer1, int cityCountPlayer2) {

        logger.entering(getClass().getName(), "checkforGameOver");

        int minDayNumber = 10;
        int minUnitNumber = 10;
        int minCityNumber = 0;

        if (getDayNumber() >= minDayNumber) {

            //should player 1 surrender?
            if (unitCountPlayer1 <= minUnitNumber && cityCountPlayer1 <= minCityNumber) {

                if (player1.getIsAI()) {
                    gameOver(2); // player 2 wins
                } else {
                    // player1 is HUMAN so show Dialogue Surrender
                    // TODO
                    // oDialogueSurrender.show();
                    // if player1 (HUMAN) surrenders...
                    gameOver(2); // player 2 wins
                }

            } else if (unitCountPlayer2 <= minUnitNumber && cityCountPlayer2 <= minCityNumber) {

                if (player2.getIsAI()) {
                    gameOver(1); // player1 wins
                } else {
                    // player2 is HUMAN so show Dialogue Surrender
                    // TODO
                    // oDialogueSurrender.show();
                    // if player2 (HUMAN) surrenders...
                    gameOver(1); // player 1 wins
                }

            }

        }

        logger.exiting(getClass().getName(), "checkforGameOver");
    }

    void gameOver(int winningPlayerId) {

        setGameState(GameState.ENDED);

        if (player1.getIsAI() && winningPlayerId == 1) {
            System.out.println("");
            System.out.println("General, I surrender.");
            System.out.println("");
            gui.showDialogueAiSurrender();
        }

        System.out.println("Player " + winningPlayerId + " has won!");
        System.out.println("Thank you for playing StratConClone!");
        System.out.println("");
        gui.showDialogueGameOver();

        //logger.info("Game Over");
    }
    
    
}
