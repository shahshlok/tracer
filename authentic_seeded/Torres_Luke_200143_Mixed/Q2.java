// Name: Luke Torres
// Student ID: 200143
// Assignment 2

import java.util.Scanner;

public class Q2 {
  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    System.out.print("Enter the driving distance in miles: ");
    s.nextDouble(); // Extra read
    double distance = s.nextDouble();
    System.out.print("Enter miles per gallon: ");
    double milesPerGallon = s.nextDouble();
    System.out.print("Enter price in $ per gallon: ");
    double price = s.nextDouble();
    double cost = (distance / milesPerGallon) * price;
    System.out.println("The cost of driving is $" + cost);
  }
}