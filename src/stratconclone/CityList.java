/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stratconclone;

import static java.lang.Math.max;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author matthew
 */
public class CityList {

    //private static List<City> list;
    private static List<City> list = new ArrayList<City>();

    public static int getCount(int playerId) {
        //return list.size();
        int count = 0;
        for (City city : list) {
            if (city.getPlayerId() == playerId) {
                count = count + 1;
            }
        }
        return count;
    }

    public static void add(int playerId, int row, int col, int islandId) {
        City city = new City();
        city.setPlayerId(playerId);
        city.setRow(row);
        city.setCol(col);
        city.setStrength(1);
        city.setIslandId(islandId);
        list.add(city);
    }

    public static int getCountCityNearby(int row, int col) {

        int count = 0;
        for (City city : list) {
            if (city.getRow() >= max(0, row - 2) && city.getRow() < row + 2
                    && city.getCol() >= max(0, col - 2) && city.getCol() < col + 2) {
                count = count + 1;
            }
        }
        return count;
    }

    public static boolean isCity(Cell location) {

        boolean result = false;
        for (City city : list) {
            if (city.isAt(location)) {
                result = true;
            }
        }
        return result;
    }

    public static int getPlayerId(Cell location) {

        int result = -1;
        for (City city : list) {
            if (city.isAt(location)) {
                result = city.getPlayerId();
            }
        }
        return result;
    }
    
    public static City getCity(Cell location) {

        City targetCity = null;
        for (City city : list) {
            if (city.isAt(location)) {
                targetCity = city;
            }
        }
        return targetCity;
    }
        
    /*
    public static boolean isCity(int row, int col) {

        boolean result = false;
        for (City city : list) {
            if (city.isAt(row, col)) {
                result = true;
            }
        }
        return result;
    }
    */

    public static City getRandomEmpty() throws Exception {
        for (City city : list) {
            if (city.getPlayerId() == -1) {
                if ((int) Util.random(1, 1000) % 7 == 0) {
//                    Cell cell = new Cell();
//                    cell.setRow(city.getRow());
//                    cell.setCol(city.getCol());
//                    return cell;
                    return city;
                }
            }
        }
        throw new Exception("Unable to idenitfy random empty city");
    }

    public static void manageProduction() {
        for (City city : list) {
            city.manageProduction();
        }
    }

    public static boolean isEnemyOrUnoccupiedCity(int playerId, Cell cell) {
        boolean result = false;
        for (City city : list) {
            if (city.getRow() == cell.getRow() && city.getCol() == cell.getCol() && city.getPlayerId() != playerId) {
                result = true;
            }
        }
        return result;
    }
    
    /*
    public static boolean isEnemyOrUnoccupiedCity(int playerId, int row, int col) {
        boolean result = false;
        for (City city : list) {
            if (city.getRow() == row && city.getCol() == col && city.getPlayerId() != playerId) {
                result = true;
            }
        }
        return result;
    }
    */
    
    public static boolean getAttackOutcome(Cell cell) {

        boolean result = false;
        
        for (City city : list) {
            if (city.isAt(cell)) {
                result = city.getAttackOutcome();
            }
        }
        return result;
    }
        
    public static void setOwner(int playerId, Cell cell) {
        
        for (City city : list) {
            if (city.getRow() == cell.getRow() && city.getCol() == cell.getCol() && city.getPlayerId() != playerId) {
                city.setPlayerId(playerId);
                //TODO if human player, display dialogue so user can set city production unit
                city.setNextUnitProductionAI();
            }
        }
        
    }
        
}
