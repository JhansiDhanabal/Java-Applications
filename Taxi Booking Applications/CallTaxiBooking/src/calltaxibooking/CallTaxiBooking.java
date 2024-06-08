/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package calltaxibooking;

import java.util.*;

/**
 *
 * @author nanda
 */
public class CallTaxiBooking extends Thread{
    static Scanner sc = new Scanner(System.in);
    ArrayList<Customer>customerlist = new ArrayList<>();
    static public Taxies taxies=new Taxies(4);
    /**
     * @param args the command line arguments
     */
    public void getInput(){
        System.out.println("_____________TAXI BOOKING APPLICATION_______________");
        System.out.println("1.Booking");
        System.out.println("2.Available Taxi");
        System.out.println("3.Display Nearby Taxi");
        System.out.println("4.Revenue Details");
        System.out.println("5.Cancellation");
        System.out.println("6.Exit");
        System.out.println("enter your choice");
        int ch = sc.nextInt();
        switch(ch){
            case 1:
            {
                System.out.println("Enter Name: ");
                String name = sc.next();
                System.out.println("Enter Starting Point");
                char start = sc.next().charAt(0);
                System.out.println("Enter Destination Point");
                char end = sc.next().charAt(0);
                Customer cust = new Customer(start, end, name);
                double amt = calculateamount(cust.startingpoint, cust.endpoint, cust);
                if(amt>0 && amt!=2){
                    cust.amount_pay = amt;
                    customerlist.add(cust);
                }
                getInput();
                break;
            }
            case 2:
            {
                available_taxi();
                getInput();
                break;
            }
            case 3:
            {
                System.out.println("Enter your Location : (A-F)");
                char location = sc.next().charAt(0);
                findnearest(location);
                getInput();
                break;
            }
            case 4:
            {
                revenue();
                getInput();
                break;
            }
            case 5:
            {
                System.out.println("Enter customer name : ");
                String name = sc.next();
                cancellation(name);
                getInput();
                break;
            }
            case 6:
                break;
            default:
                System.out.println("Incorrect Password");
                getInput();
        }
        
    }
    //cancell the booking ticket
    public void cancellation(String name){
        boolean find  = false;
        for(Customer user:customerlist){
            if(user.name.equals(name)){
                Taxi dummy = user.assigned;
                double refund = user.amount_pay;
                System.out.println("Refunded Amount : "+refund);
                dummy.earn -= refund;
                if(user.ratings>3)
                    dummy.earn -= (user.ratings-3)*5;
                dummy.isfree = true;
                dummy.cust.remove(user);
                customerlist.remove(user);
                find = true;
                break;
            }
        }
        if(find==false)
            System.out.println("YOU DIDNT REGISTERED (write username corretly)");
    }
    // To see the revenue of each taxi and entire company
    public void revenue(){
        Taxi t = new Taxi();
        if(t.total_earn>0){
            System.out.println("Revenue Details");
            System.out.println("--------------------------------------------");
            for(Taxi taxi:taxies.taxilist){
                if(taxi.earn>0){
                    System.out.println("--------------------------------------------");
                    System.out.println(taxi.name+" Details: Points Earned : "+taxi.point);
                    System.out.println("Customer Name   Starting Point   End Point   Amount  Ratings");
                    for(Customer c:taxi.cust){
                        System.out.println(c.name+"         "+c.startingpoint+"             "
                                +c.endpoint+"           "+c.amount_pay+"           "+c.ratings);
                    }
                    System.out.println("Total Earnings : "+taxi.earn);
                    System.out.println("--------------------------------------------");
                }
            }
            System.out.println("Total Earning : "+t.total_earn);
        }
        else
            System.out.println("Booking is not done");
    }
    //find nearest taxi available based on the given city preference
    public void findnearest(char start){
        ArrayList<Taxi>nearestTaxies = new ArrayList<>();
        ArrayList<Taxi>other = new ArrayList<>();
        for(Taxi taxi:taxies.taxilist){
            if(taxi.isfree){
                if(taxi.CurrentPoint==start)
                {
                    if(nearestTaxies.isEmpty())
                       nearestTaxies.add(taxi);
                    else if(nearestTaxies.get(0).earn>taxi.earn)
                        nearestTaxies.add(0, taxi);
                    else
                        nearestTaxies.add(taxi);
                }
                else{
                    if(other.isEmpty())
                        other.add(taxi);
                    else if(Math.abs(other.get(0).CurrentPoint-start)>Math.abs(start-taxi.CurrentPoint))
                        other.add(0, taxi);
                    else if(other.get(0).CurrentPoint==taxi.CurrentPoint){
                        if(other.get(0).earn>taxi.earn)
                            other.add(0,taxi);
                        else
                            other.add(taxi);
                    }
                    else
                        other.add(taxi);
                }
            }
        }
        for(Taxi t:nearestTaxies)
            System.out.println(t);
        for(Taxi t:other)
            System.out.println(t);

        
    }
    //display the free taxi
    public void available_taxi() {
        for(Taxi taxi : taxies.taxilist){
            if(taxi.isfree)
                System.out.println(taxi);
        }
    }
    //performs method to see the available taxi and return the amount to pay
    public double calculateamount(char start, char end, Customer cust){
        if(end<start)
        {
            char temp = start;
            start = end;
            end = temp;
        }
        if(start<'A' || end >'F')
        {
            System.out.println("You select Outer Areas");
            System.out.println("Please Select areas from A-F");
            return -2;
        }
        double amt = 0;
        ArrayList<Taxi>nearestTaxies = new ArrayList<>();
        ArrayList<Taxi>samelocation = new ArrayList<>();
        for(Taxi taxi:taxies.taxilist){
            if(taxi.isfree){
                if(taxi.CurrentPoint==start){
                    samelocation.add(taxi);
                }
                if(Math.abs(start-taxi.CurrentPoint)==1)
                    nearestTaxies.add(0, taxi);
                else
                    nearestTaxies.add(taxi);
            }
        }
        if(samelocation.isEmpty()==false){
            Taxi taxi = find_min(samelocation);
            amt = assign(taxi, start, end, cust);
            System.out.println("Taxi "+taxi.name+" is assigned pay rupees "+amt);
            
            System.out.println("Enter your Ratings (1-5): ");
            int rate = sc.nextInt();
            cust.ratings = rate;
            if(rate<3){
                taxi.point -= 1;
            }
            else{
                rate -= 3;
                taxi.point += rate;
                taxi.earn += rate*5;
                 taxi.total_earn += rate*5;
            }
            if(taxi.point<=0){
                taxi.earn -= 5;
                taxi.total_earn -= 5;
            }
            Scheduling sd = new Scheduling(taxi, end-start);
            sd.start();
            return amt;
        }
        if(nearestTaxies.isEmpty() == true)
        {
            System.out.println("Sorry no taxi is free");
            return amt;
        }
        else if(nearestTaxies.size()==1)
        {
            amt = assign(nearestTaxies.get(0), start, end, cust);
            System.out.println("Taxi "+nearestTaxies.get(0).name+" is assigned pay rupees "+amt);
            System.out.println("Enter your Ratings (1-5): ");
            int rate = sc.nextInt();
            cust.ratings = rate;
            if(rate<3){
                nearestTaxies.get(0).point -= 1;
            }
            else{
                rate -= 3;
                nearestTaxies.get(0).point += rate;
                nearestTaxies.get(0).earn += rate*5;
                nearestTaxies.get(0).total_earn += rate*5;
            }
            if(nearestTaxies.get(0).point<=0){
                nearestTaxies.get(0).earn -= 5;
                nearestTaxies.get(0).total_earn -= 5;
            }
            Scheduling sd = new Scheduling(nearestTaxies.get(0), end-start);
            sd.start();
        
            return amt;
        }
        else{
            Taxi min = nearestTaxies.get(0);
            for(Taxi taxi : nearestTaxies){
                if(taxi.CurrentPoint==min.CurrentPoint){
                    if(taxi.earn<min.earn)
                        min = taxi;
                }
                else
                    break;
            }
            amt = assign(min, start, end, cust);
            System.out.println("Taxi "+min.name+" is assigned pay rupees "+amt);
            
            System.out.println("Enter your Ratings (1-5): ");
            int rate = sc.nextInt();
            cust.ratings = rate;
            if(rate<3){
                min.point -= 1;
            }
            else{
                rate -= 3;
                min.point += rate;
                min.earn += rate*5;
                min.total_earn += rate*5;
            }
            if(min.point<=0){
                min.earn -= 5;
                min.total_earn -= 5;
            }
            Scheduling sd = new Scheduling(min, end-start);
            sd.start();
        
            return amt;
        }
    }
    
    public Taxi find_min(ArrayList<Taxi> arr){
        Taxi taxi = arr.get(0);
        for(Taxi t:arr){
            if(t.earn<taxi.earn){
                taxi = t;
            }
        }
        return taxi;
    }
    //assign the taxi to particular taxi to driver
    public double assign(Taxi taxi, char start, char end, Customer customer){
        double amt;
        amt = 100*5+((end-start)*15-5)*10;
        taxi.CurrentPoint = end;
        taxi.earn += amt;
        taxi.total_earn += amt;
        taxi.cust.add(customer);
        customer.assigned = taxi;
        return amt;
    }
    public static void main(String[] args) {
        // TODO code application logic here
        
        CallTaxiBooking booking = new CallTaxiBooking();
        booking.getInput();
    }

    
}
