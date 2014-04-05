/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

public class Main {

    private static GUI gui;

    private static GameEngine gameEngine;
    private static Grid grid;

    // desired fps
    private final static int MAX_FPS = 2;
    // maximum number of frames to be skipped
    private final static int MAX_FRAME_SKIPS = 4;
    // the frame period
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    public static void main(String args[]) {

        boolean isPortCityPlayer1, isPortCityPlayer2;
        boolean maximiseGameWindow = true;
        
        try {

            gameEngine = new GameEngine();

            gameEngine.init(0, -1);
            grid.init();
            
            isPortCityPlayer1 = gameEngine.assignPlayerStartCity(1, grid);
            isPortCityPlayer2 = gameEngine.assignPlayerStartCity(2, grid);
            
            gui = new GUI(grid, maximiseGameWindow); // create and display GUI
            
            gameEngine.setGui(gui);
            
            // TODO: if HUMAN player2, show select city production dialogue
            gui.show_dialogue_city_production(isPortCityPlayer2);

            gameLoop(); // start the game loop

        } catch (Exception ex) {
            //throw ex;
        }
    }

    static void gameLoop() {
        // game loop  

        long beginTime;     // the time when the cycle begun
        long timeDiff;      // the time it took for the cycle to execute
        int sleepTime;      // ms to sleep (<0 if we're behind)
        int framesSkipped;  // number of frames being skipped
        sleepTime = 0;
        boolean showFogOfWar = true;
        
        try {

            while (!gameEngine.getGameOver() && gameEngine.getDayNumber() < 50) {

                beginTime = System.currentTimeMillis();
                framesSkipped = 0;  // resetting the frames skipped

                // update game state 
                grid.render(gui.getPanelReference(), showFogOfWar, gameEngine.getIsVisiblePlayer1(), gameEngine.getIsVisiblePlayer2());
                gui.updateDayNumberLabel(gameEngine.getDayNumber());
                gameEngine.doPlayerMove(grid);

                // calculate how long did the cycle take
                timeDiff = System.currentTimeMillis() - beginTime;
                
                // calculate sleep time
                sleepTime = (int) (FRAME_PERIOD - timeDiff);

                if (sleepTime > 0) {
                    // if sleepTime > 0 we're OK
                    try {
                        // send the thread to sleep for a short period
                        // very useful for battery saving
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                    }
                }

//                while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
//                    // we need to catch up
//                    // update without rendering
//                    /////////////this.gamePanel.update();
//
//                    // add frame period to check if in next frame
//                    sleepTime += FRAME_PERIOD;
//                    framesSkipped++;
//                }
            }
        } catch (Exception ex) {
            //throw ex;
        }

    }
}
