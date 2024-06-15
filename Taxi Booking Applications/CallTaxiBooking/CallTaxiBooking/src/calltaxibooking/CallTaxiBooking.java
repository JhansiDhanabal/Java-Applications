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

    public void getInput(){
        System.out.println("\n************ TAXI BOOKING APPLICATION ************");
        System.out.println("1. Book a Taxi");
        System.out.println("2. Check Available Taxis");
        System.out.println("3. Display Nearby Taxis");
        System.out.println("4. View Revenue Details");
        System.out.println("5. Cancel a Booking");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
        int ch = sc.nextInt();
        switch(ch){
            case 1:
                bookTaxi();
                break;
            case 2:
                displayAvailableTaxis();
                break;
            case 3:
                displayNearbyTaxis();
                break;
            case 4:
                viewRevenueDetails();
                break;
            case 5:
                cancelBooking();
                break;
            case 6:
                System.out.println("Exiting application. Thank you!");
                return;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
        getInput();
    }

    private void bookTaxi() {
        System.out.print("Enter Name: ");
        String name = sc.next();
        System.out.print("Enter Starting Point: ");
        char start = sc.next().charAt(0);
        System.out.print("Enter Destination Point: ");
        char end = sc.next().charAt(0);
        Customer cust = new Customer(start, end, name);
        double amt = calculateAmount(cust.startingpoint, cust.endpoint, cust);
        if(amt > 0 && amt != 2){
            cust.amount_pay = amt;
            customerlist.add(cust);
            System.out.printf("Booking successful! Amount to be paid: Rs. %.2f\n", amt);
        }
    }

    private void displayAvailableTaxis() {
        System.out.println("Available Taxis:");
        System.out.println("--------------------------------------------");
        for(Taxi taxi : taxies.taxilist){
            if(taxi.isfree)
                System.out.println(taxi);
        }
        System.out.println("--------------------------------------------");
    }

    private void displayNearbyTaxis() {
        System.out.print("Enter your Location (A-F): ");
        char location = sc.next().charAt(0);
        findNearest(location);
    }

    private void viewRevenueDetails() {
        revenue();
    }

    private void cancelBooking() {
        System.out.print("Enter customer name: ");
        String name = sc.next();
        cancellation(name);
    }

    public void cancellation(String name){
        boolean find = false;
        for(Customer user : customerlist){
            if(user.name.equals(name)){
                Taxi dummy = user.assigned;
                double refund = user.amount_pay;
                System.out.printf("Refunded Amount: Rs. %.2f\n", refund);
                dummy.earn -= refund;
                dummy.total_earn -= refund;
                if(user.ratings > 3){
                    dummy.earn -= (user.ratings - 3) * 5;
                    dummy.total_earn -= (user.ratings - 3) * 5;
                }
                dummy.isfree = true;
                dummy.cust.remove(user);
                customerlist.remove(user);
                find = true;
                break;
            }
        }
        if(!find)
            System.out.println("You are not registered (please check the username).");
    }

    public void revenue(){
        Taxi t = new Taxi();
        if(t.total_earn > 0){
            System.out.println("Revenue Details");
            System.out.println("--------------------------------------------");
            for(Taxi taxi : taxies.taxilist){
                if(taxi.earn > 0){
                    System.out.println(taxi.name + " Details: Points Earned: " + taxi.point);
                    System.out.println("Customer Name   Starting Point   End Point   Amount  Ratings");
                    for(Customer c : taxi.cust){
                        System.out.printf("%s         %c             %c           %.2f           %d\n", c.name, c.startingpoint, c.endpoint, c.amount_pay, c.ratings);
                    }
                    System.out.printf("Total Earnings: Rs. %.2f\n", taxi.earn);
                    System.out.println("--------------------------------------------");
                }
            }
            System.out.printf("Total Earnings: Rs."+t.total_earn);
        } else {
            System.out.println("No bookings have been made.");
        }
    }

    public void findNearest(char start){
        ArrayList<Taxi> nearestTaxies = new ArrayList<>();
        ArrayList<Taxi> other = new ArrayList<>();
        for(Taxi taxi : taxies.taxilist){
            if(taxi.isfree){
                if(taxi.CurrentPoint == start){
                    if(nearestTaxies.isEmpty())
                       nearestTaxies.add(taxi);
                    else if(nearestTaxies.get(0).earn > taxi.earn)
                        nearestTaxies.add(0, taxi);
                    else
                        nearestTaxies.add(taxi);
                } else {
                    if(other.isEmpty())
                        other.add(taxi);
                    else if(Math.abs(other.get(0).CurrentPoint - start) > Math.abs(start - taxi.CurrentPoint))
                        other.add(0, taxi);
                    else if(other.get(0).CurrentPoint == taxi.CurrentPoint){
                        if(other.get(0).earn > taxi.earn)
                            other.add(0, taxi);
                        else
                            other.add(taxi);
                    } else {
                        other.add(taxi);
                    }
                }
            }
        }
        System.out.println("Nearby Taxis:");
        System.out.println("--------------------------------------------");
        for(Taxi t : nearestTaxies)
            System.out.println(t);
        for(Taxi t : other)
            System.out.println(t);
        System.out.println("--------------------------------------------");
    }

    public double calculateAmount(char start, char end, Customer cust){
        if(end < start){
            char temp = start;
            start = end;
            end = temp;
        }
        if(start < 'A' || end > 'F'){
            System.out.println("Selected areas are out of range. Please select areas between A and F.");
            return -2;
        }
        double amt = 0;
        ArrayList<Taxi> nearestTaxies = new ArrayList<>();
        ArrayList<Taxi> sameLocation = new ArrayList<>();
        for(Taxi taxi : taxies.taxilist){
            if(taxi.isfree){
                if(taxi.CurrentPoint == start){
                    sameLocation.add(taxi);
                }
                if(Math.abs(start - taxi.CurrentPoint) == 1)
                    nearestTaxies.add(0, taxi);
                else
                    nearestTaxies.add(taxi);
            }
        }
        if(!sameLocation.isEmpty()){
            Taxi taxi = findMin(sameLocation);
            amt = assign(taxi, start, end, cust);
            System.out.printf("Taxi %s is assigned. Please pay Rs. %.2f\n", taxi.name, amt);
            requestRating(cust, taxi, start, end);
            return amt;
        }
        if(nearestTaxies.isEmpty()){
            System.out.println("Sorry, no taxis are available.");
            return amt;
        } else {
            Taxi taxi = findMin(nearestTaxies);
            amt = assign(taxi, start, end, cust);
            System.out.printf("Taxi %s is assigned. Please pay Rs. %.2f\n", taxi.name, amt);
            requestRating(cust, taxi, start, end);
            return amt;
        }
    }

    private Taxi findMin(ArrayList<Taxi> arr){
        Taxi taxi = arr.get(0);
        for(Taxi t : arr){
            if(t.earn < taxi.earn){
                taxi = t;
            }
        }
        return taxi;
    }

    private double assign(Taxi taxi, char start, char end, Customer customer){
        double amt = 100 * 5 + ((end - start) * 15 - 5) * 10;
        taxi.CurrentPoint = end;
        taxi.earn += amt;
        taxi.total_earn += amt;
        taxi.cust.add(customer);
        customer.assigned = taxi;
        return amt;
    }

    private void requestRating(Customer cust, Taxi taxi, char start, char end) {
        System.out.print("Enter your Ratings (1-5): ");
        int rate = sc.nextInt();
        cust.ratings = rate;
        if(rate < 3){
            taxi.point -= 1;
        } else {
            rate -= 3;
            taxi.point += rate;
            taxi.earn += rate * 5;
            taxi.total_earn += rate * 5;
        }
        if(taxi.point <= 0){
            taxi.earn -= 5;
            taxi.total_earn -= 5;
        }
        Scheduling sd = new Scheduling(taxi, end - start);
        sd.start();
    }

    public static void main(String[] args) {
        CallTaxiBooking booking = new CallTaxiBooking();
        booking.getInput();
    }
}
