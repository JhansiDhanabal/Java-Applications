/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calltaxibooking;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nanda
 */
public class Scheduling extends Thread{
    Taxi taxi;
    int time = 1;
    Scheduling(Taxi t, int time){
        taxi = t;
        this.time = time;
    }
    @Override
    public void run(){
        taxi.isfree = false;
        try {
            Thread.sleep(6000*time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Scheduling.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
        taxi.isfree = true;
        }
    }
}
