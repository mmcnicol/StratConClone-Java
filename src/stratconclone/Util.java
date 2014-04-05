/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package stratconclone;

import java.util.Random;

/**
 *
 * @author matthew
 */
public class Util {
    
    private static Random rand = new Random();
    
     public static int random(int min, int max) {
        return rand.nextInt(max) + min;
    }
     
}
