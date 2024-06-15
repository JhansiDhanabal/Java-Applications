/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calltaxibooking;

/**
 *
 * @author nanda
 */
public class Customer extends Thread{
    static int id = 0;
    char startingpoint;
    char endpoint;
    String name;
    double amount_pay=0;
    int ratings = 0;
    Taxi assigned = null;
    Customer(char s, char e, String name){
        startingpoint = s;
        endpoint = e;
        this.name = name;
        id+=1;
    }
}
