/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calltaxibooking;

import java.util.ArrayList;

/**
 *
 * @author nanda
 */
public class Taxi extends Thread{
    public static int total_earn = 0;
    char CurrentPoint = 'A';
    double earn = 0;
    static int id = 0;
    boolean isfree = true;
    String name = "Taxi-";
    int point = 2;
    ArrayList<Customer> cust = new ArrayList<>();

    Taxi(){
        id += 1;
        name += id;
    }

    @Override
    public String toString(){
        return String.format("%s (Current Point: %c, Earnings: Rs. %.2f)", name, CurrentPoint, earn);
    }
}
