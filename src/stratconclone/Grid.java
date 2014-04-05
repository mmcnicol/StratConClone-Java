/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import java.util.ArrayList;
//import java.util.Random;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.cos;
import static java.lang.Math.round;
import static java.lang.Math.ceil;
import java.util.logging.Logger;
import java.util.logging.Handler;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.FileHandler;
//import javax.swing.SwingUtilities;
//import javax.swing.JFrame;
import javax.swing.JPanel;
//import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Font;
//import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseMotionListener;
//import java.awt.event.MouseMotionAdapter;
//import static javaapplication3;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.floor;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author matthew
 */
public class Grid {

    private static final int rows = 100, cols = 100;

    private static ArrayList listNeighbour; // used by algorithm to identify actual islands
    private static int showFromRow = 0;
    private static int showFromCol = 0;
    private static int showFromRowRealtime = 0;
    private static int showFromColRealtime = 0;

    //Cell cell[][] = new Cell[99][99];
    private static Cell[][] cell = new Cell[rows][cols];

    private final static Logger logger = Logger.getLogger(GUI.class.getName());

    private static BufferedImage imageCity0;
    private static BufferedImage imageCity1;
    private static BufferedImage imageCity2;
    private static BufferedImage imageLand;
    private static BufferedImage imageSea;
    private static BufferedImage imageTank1;
    private static BufferedImage imageTank2;

    public static void scrollEvent(int horizontal, int vertical) {

        showFromRowRealtime = (int) floor(vertical * 2);
        showFromColRealtime = (int) floor(horizontal * 2);
    }

    //getters & setters
    public static int getRows() {
        return rows;
    }

    public static int getCols() {
        return cols;
    }

    public static boolean isLand(int row, int col) {
        return cell[row][col].isLand();
    }

    public static boolean isSea(int row, int col) {
        return cell[row][col].isSea();
    }

    public static boolean isFogOfWar(int playerId, Cell location) {
        return cell[location.getRow()][location.getCol()].getFogOfWar(playerId);
    }

    /*
     public static boolean isFogOfWar(int playerId, int row, int col) {
     return cell[row][col].getFogOfWar(playerId);
     }
     */
    public static void clearFogOfWar(int playerId, int row, int col) {
        for (int r = max(0, row - 1); r <= min(rows, row + 1); r = r + 1) {
            for (int c = max(0, col - 1); c <= min(cols, col + 1); c = c + 1) {
                cell[r][c].setFogOfWar(playerId, false);
            }
        }
    }

    public static boolean isNextToLand(int row, int col) {

        boolean result = false;

        for (int r = max(0, row - 1); r <= min(rows, row + 1); r = r + 1) {
            for (int c = max(0, col - 1); c <= min(cols, col + 1); c = c + 1) {
                if (isLand(r, c)) {
                    result = true;
                }
            }
        }

        return result;
    }

    public static void init() {

        logger.addHandler(new ConsoleHandler());

        //logger.setLevel(Level.INFO);
        logger.setLevel(Level.ALL);

        //logger.entering(getClass().getName(), "init");
        initGridToSea();
        generateIslands();

        listNeighbour = new ArrayList();
        identifyIslands(); // some islands might overlap so idenitfy distinct islands
        generateCities();
        //assignPlayerStartCity(1);
        //assignPlayerStartCity(2);

        //logger.exiting(getClass().getName(), "init");
        try {
            imageCity0 = ImageIO.read(new File("images/city0.png"));
            imageCity1 = ImageIO.read(new File("images/city1.png"));
            imageCity2 = ImageIO.read(new File("images/city2.png"));
            imageLand = ImageIO.read(new File("images/land.png"));
            imageSea = ImageIO.read(new File("images/sea.png"));
            imageTank1 = ImageIO.read(new File("images/tank1.png"));
            imageTank2 = ImageIO.read(new File("images/tank2.png"));
        } catch (IOException ex) {
            // handle exception...
        }
    }

    public static void initGridToSea() {

        // initialise grid to sea
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cell[r][c] = new Cell();
                cell[r][c].setRow(r);
                cell[r][c].setCol(c);
                cell[r][c].setType(Cell.Type.SEA);
                cell[r][c].setFogOfWar(1, true);
                cell[r][c].setFogOfWar(2, true);
                cell[r][c].setIslandId(-1);
            }
        }
    }

    private static void generateIslands() {

        int row, col;

        //defineIslandAsRandomPoly(1, 16 * (int) random(18, 25), 16 * (int) random(8, 14), 16 * (int) random(4, 5), 16 * (int) random(4, 5));
        //defineIslandAsRandomPoly(2, 16 * (int) random(4, 8), 16 * (int) random(15, 25), 16 * (int) random(4, 6), 16 * (int) random(5, 7));
        for (row = 5; row < rows; row = row + 8) {
            for (col = 5; col < cols; col = col + 6) {
                //switch( (int)random(1,3) ) {
                //	case 1:
                if ((int) Util.random(1, 1000) % 3 == 0) {
                    defineIslandAsRandomPoly(16 * (int) Util.random(row - 5, row + 5), 16 * (int) Util.random(col - 5, col + 5), 16 * (int) Util.random(3, 5), 16 * (int) Util.random(3, 5));
                }
                /*		break;
                 case 2:
                 if ((int)random(1,1000)%2==0) defineIslandAsRandomPoly(-1, 16*(int)random(i-5,i+5), 16*(int)random(j-5,j+5), 16*(int)random(4,6), 16*(int)random(4,6) );
                 break;
                 case 3:
                 if ((int)random(1,1000)%2==0) defineIslandAsRandomPoly(-1, 16*(int)random(i-5,i+5), 16*(int)random(j-5,j+5), 16*(int)random(6,10), 16*(int)random(6,10) );
                 break;
                 } 
                 */
            }
        }
    }

    private static void defineIslandAsRandomPoly(int X, int Y, double w, double h) {

        //logger.info("in defineIslandAsRandomPoly(" + X + ", " + Y + ")");
        //int islandListId;
        //oHelloWorld.Draw();
        //oCityList.AddCity(intPlayerId, X, Y);
        //IslandList.add(playerId);
        //islandListId = IslandList.getCount(-1);
        // create a poly (island)
        float deg = 0, deg2;
        int x, y, xx, yy, xxx, yyy, rand_number, count = 0, prevX, prevY, cityCount = 0;

        int[] intX = new int[50];
        int[] intY = new int[50];

        int a = 0, b = 0;

        do {
            //console.log( "count=" + count );

            x = (int) round(w * cos(deg));
            y = (int) round(h * sin(deg));

            a = X + x;
            b = Y + y;
            intX[count] = X + x;
            intY[count] = Y + y;

            prevX = a;
            prevY = b;

            rand_number = Util.random(5, 8);
            deg2 = (float) rand_number / 10;
            //deg += 0.5; //0.005;
            deg += deg2;
            count = count + 1;
        } while (deg <= 6.4 && count < 50);

        //int maxCityPerIsland = (int) random(2, 8);
        // for each cell that is within the rectangle (that contains the poly)
        // credits: amended version of 'point in poly' algorithm by Randolph Franklin
        for (xx = X - (int) w; xx <= (int) X + (int) w; xx = xx + 16) {

            for (yy = Y - (int) h; yy <= (int) Y + (int) h; yy = yy + 16) {

                // next identify if this cell is within the poly (island)
                int npol = count, x2 = xx + 8, y2 = yy + 8;

                int i, j;
                boolean c = false;

                for (i = 0, j = npol - 1; i < npol; j = i++) {

                    int intXI = intX[i];
                    int intXJ = intX[j];

                    int intYI = intY[i];
                    int intYJ = intY[j];

                    if ((((intYI <= y2) && (y2 < intYJ))
                            || ((intYJ <= y2) && (y2 < intYI)))
                            && (x2 < (intXJ - intXI) * (y2 - intYI) / (intYJ - intYI) + intXI)) {
                        c = !c;
                    }

                }

                // if point is in poly, mark GridCellType as Land (code 1)
                if (c) {
                    //image( imgLand, xx+1, yy+1 ); 
                    //intGridCellType[(int)xx/16][(int)yy/16]=1;
                    xxx = (int) ceil((xx) / 16);
                    yyy = (int) ceil((yy) / 16);

                    if (xxx >= 0 && yyy >= 0 && xxx < rows && yyy < cols) {
                        //intGridCellType[xxx][yyy] = 1;
                        cell[xxx][yyy].setType(Cell.Type.LAND);
                        //logger.info("in cell " + xxx + ", " + yyy + " set to land");

                    }

                }

            }

        }

    }

    private static void identifyIslands() {

        int row, col, islandListId = 0;

        for (row = 0; row < rows; row = row + 1) {
            for (col = 0; col < cols; col = col + 1) {

                if (cell[row][col].isLand() && cell[row][col].getIslandId() == -1) {
                    cell[row][col].setIslandId(islandListId);
                    //intGridIslands[x][y] = iIslandListId;
                    updateGridIslandsDetail(row, col, islandListId);

                    while (listNeighbour.size() > 0) {

                        for (int i = 0; i < listNeighbour.size(); i++) {
                            Cell cell = (Cell) listNeighbour.get(i);
                            updateGridIslandsDetail(cell.getRow(), cell.getCol(), islandListId);
                            listNeighbour.remove(i);
                        }

                    }

                    IslandList.add(-1); // add an island to IslandList which is not assigned to any player

                    islandListId = islandListId + 1;

                }
            }
        }
    }

    private static void updateGridIslandsDetail(int row, int col, int islandListId) {

        for (int r = max(0, row - 1); r <= min(rows - 1, row + 1); r = r + 1) {
            for (int c = max(0, col - 1); c <= min(cols - 1, col + 1); c = c + 1) {

                if (cell[r][c].isLand() && cell[r][c].getIslandId() == -1) {
                    cell[r][c].setIslandId(islandListId);
                    Cell cell = new Cell();
                    cell.setRow(r);
                    cell.setCol(c);
                    listNeighbour.add(cell);
                }

            }
        }
    }

    public static void generateCities() {

        // for each island
        int islandCount = IslandList.getCount();

        for (int i = 0; i < islandCount; i = i + 1) {

            AddCitiesToIslandListId(i);
        }

    }

    public static void AddCitiesToIslandListId(int islandListId) {

        int row, col;
        int maxCityPerIsland = (int) Util.random(3, 8);
        int cityCount = 0;

        // scan through grid
        for (row = 0; row < rows; row = row + 1) {
            for (col = 0; col < cols; col = col + 1) {

                if (cell[row][col].getIslandId() == islandListId) {

                    if (((int) Util.random(1, 1000) % 7 == 0) && cityCount < maxCityPerIsland) {

                        // game rule: cities should not be immediately next to each other
                        if (CityList.getCountCityNearby(row, col) == 0) {

                            CityList.add(-1, row, col, islandListId);

                            cityCount++;
                        }
                    }

                }

            }
        }
    }

    public static void display() {

        Cell location = new Cell();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                location.setRow(r);
                location.setCol(c);
//                if (cell[r][c].getFogOfWar(1)) {
//                    System.out.print("X");
//                } else if (cell[r][c].isSea()) {
                if (cell[r][c].isSea()) {
                    System.out.print("S");
                } else if (CityList.isCity(location)) {
                    System.out.print("C");
                } else if (cell[r][c].isLand()) {
                    //System.out.print("L");
                    System.out.print(cell[r][c].getIslandId());
                } else {
                    System.out.print("?");
                }
            }
            System.out.println("");
        }
    }

    public static int mapToViewportRow(int i) {
        return ((i + 1) * 16) - 16 - (showFromRow * 16);
        //return i;
    }

    public static int mapToViewportCol(int i) {
        return ((i + 1) * 16) - 16 - (showFromCol * 16);
        //return i;
    }

    public static void render(JPanel p, boolean showFogOfWar, boolean isVisiblePlayer1, boolean isVisiblePlayer2) {

        int playerId = 1;
        Cell location = new Cell();
        boolean skip;
        int fontSize;
        int r, c;

        //Image bufferimage = p.createImage(1400, 700);
        Image bufferimage = p.createImage(rows * 16, cols * 16);
        Graphics dbg = bufferimage.getGraphics();

        //clean the screen
        dbg.setColor(new Color(100, 100, 100));
        dbg.fillRect(0, 0, p.getWidth(), p.getHeight());

        // Origin is on the upper left
        // Positive X goes “right”
        // Positive Y goes “down”
        // snapshot scroll bar values prior to rendering
        showFromRow = showFromRowRealtime;
        showFromCol = showFromColRealtime;

        fontSize = 6;
        dbg.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        
        
//        g.setColor(Color.BLACK);
//        g.fillRect(1, 1, 698, 698);

        //g.setColor(Color.WHITE);
        for (r = showFromRow; r < rows; r++) {
            for (c = showFromCol; c < cols; c++) {

                location.setRow(r);
                location.setCol(c);

                // just testing - hide fog of war so  can see what the AI units are doing
//                if (cell[r][c].getFogOfWar(1) || cell[r][c].getFogOfWar(2)) {
//                    //System.out.print("X");
//                    //g.drawString("X", mapTo(r), mapTo(c));
//
//                } else 
                skip = false;
                if (showFogOfWar) {
                    skip = true;
                    
                    if (!cell[r][c].getFogOfWar(1)) {
                        skip = false;
                    }
                    if (!cell[r][c].getFogOfWar(2)) {
                        skip = false;
                    }
                }
                if (!skip) {
                    if (cell[r][c].isSea()) {
                    //if (cell[r][c].isSea()) {
                        //g.drawString("S", mapTo(r), mapTo(c));
                        dbg.drawImage(imageSea, mapToViewportCol(c), mapToViewportRow(r), null);

                    } else if (CityList.isCity(location)) {
                        //g.drawString("C", mapTo(r), mapTo(c));
                        dbg.drawImage(imageCity0, mapToViewportCol(c), mapToViewportRow(r), null);

                        City city = CityList.getCity(location);
                        
                        switch (city.getPlayerId()) {
                            case 1:
                                dbg.drawImage(imageCity1, mapToViewportCol(c), mapToViewportRow(r), null);
                                break;
                            case 2:
                                dbg.drawImage(imageCity2, mapToViewportCol(c), mapToViewportRow(r), null);
                                break;
                            default:
                                dbg.drawImage(imageCity0, mapToViewportCol(c), mapToViewportRow(r), null);
                                break;
                        }
                        
                        // if city is occupied, display productionDaysLeft
                        if(city.isOccupied()) {
                            dbg.setColor(new Color(255, 255, 255)); // white
                            if (city.getProductionDaysLeft()<10)
                                dbg.fillRect(mapToViewportCol(c) +2, mapToViewportRow(r) +2, 5, 8);
                            else 
                                dbg.fillRect(mapToViewportCol(c) +2, mapToViewportRow(r) +2, 10, 8);
                            dbg.setColor(new Color(100, 100, 100));
                            dbg.drawString(Integer.toString(city.getProductionDaysLeft()), mapToViewportCol(c) +3, mapToViewportRow(r) +8);
                        }

                    } else if (cell[r][c].isLand()) {
                    //System.out.print("L");
                        //String temp = Integer.toString(cell[r][c].getIslandId());
                        //g.drawString(temp, mapTo(r), mapTo(c));

                        dbg.drawImage(imageLand, mapToViewportCol(c), mapToViewportRow(r), null);

                    } else {
                        //System.out.print("?");
                    }
                }
            }
            //System.out.println("");
        }

        dbg.setColor(new Color(100, 100, 100));
        fontSize = 7;
        dbg.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));

        for (r = showFromRow + 2; r <= rows; r++) {
            dbg.drawString(Integer.toString(r), mapToViewportCol(1 + showFromCol) - 13, mapToViewportRow(r) - 4);

        }
        for (c = showFromCol + 1; c < cols; c++) {
            dbg.drawString(Integer.toString(c + 1), mapToViewportCol(c) + 3, mapToViewportRow(1 + showFromRow) - 4);
        }

        dbg.setColor(new Color(200, 200, 200));
        List<Unit> list = UnitList.getList();

        for (Unit unit : list) {
            if (unit.getPlayerId() == 1) {
                if(isVisiblePlayer1) {
                    dbg.drawImage(imageTank1, mapToViewportCol(unit.getCol()), mapToViewportRow(unit.getRow()), null);
                    
                    // if unit has a MoveTo location set, display it
                    if(unit.getMoveToRow() != -1 && unit.getMoveToCol() != -1) {
                        dbg.drawString(Integer.toString(unit.getRow())+","+Integer.toString(unit.getCol())+ "  "+Integer.toString(unit.getMoveToRow())+","+Integer.toString(unit.getMoveToCol()), 
                                mapToViewportCol(unit.getCol()), mapToViewportRow(unit.getRow()));
                        dbg.drawLine(mapToViewportCol(unit.getCol())+8, mapToViewportRow(unit.getRow())+8, 
                                mapToViewportCol(unit.getMoveToCol())+8, mapToViewportRow(unit.getMoveToRow())+8);
                    }
                    
                }
            } else if (unit.getPlayerId() == 2) {
                if(isVisiblePlayer2) {
                    dbg.drawImage(imageTank2, mapToViewportCol(unit.getCol()), mapToViewportRow(unit.getRow()), null);
                    
                    // if unit has a MoveTo location set, display it
                    if(unit.getMoveToRow() != -1 && unit.getMoveToCol() != -1) {
                        dbg.drawString(Integer.toString(unit.getRow())+","+Integer.toString(unit.getCol())+ "  "+Integer.toString(unit.getMoveToRow())+","+Integer.toString(unit.getMoveToCol()), 
                                mapToViewportCol(unit.getCol()), mapToViewportRow(unit.getRow()));
                        dbg.drawLine(mapToViewportCol(unit.getCol())+8, mapToViewportRow(unit.getRow())+8, 
                                mapToViewportCol(unit.getMoveToCol())+8, mapToViewportRow(unit.getMoveToRow())+8);
                    }
                    
                }
            }
        }

        Graphics g = p.getGraphics();
        g.drawImage(bufferimage, 0, 0, p);

        //g.drawImage(bufferimage, showFromRow*16, showFromCol*16, p);
        g.dispose();

    }

}
