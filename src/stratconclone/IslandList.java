/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stratconclone;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author matthew
 */
public class IslandList {
    
    //private static List<Island> list;
    private static List<Island> list = new ArrayList<Island>();

    public static int getCount() {
        return list.size();
//        int count=0;
//        for (Island island : list) {
//            count=count+1;
//        }
//        return count;
    }
        
    public static int getCount(int playerId) {
        //return list.size();
        int count=0;
        for (Island island : list) {
            if ( island.getPlayerId()==playerId) count=count+1;
        }
        return count;
    }
    
    public static void add(int playerId) {
        Island island = new Island();
        island.setPlayerId(playerId);
        list.add(island);
    }
    
    public static void display() {

        int count=0;
        for (Island island : list) {
            
            System.out.println("island "+count);  
            //System.out.println("island "+island.hashCode());  
            count=count+1;
        }
    }    
    
    
}
