/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calltaxibooking;

import java.util.*;

/**
 *
 * @author nanda
 */
public class Taxies {
    ArrayList<Taxi>taxilist = new ArrayList<>();
    Taxies(int n){
        for(int i=0;i<n;i++){
            Taxi taxi = new Taxi();
            taxilist.add(taxi);
        }
    }
}
