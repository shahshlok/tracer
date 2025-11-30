// Name: Samuel Rivera
// Student ID: 200103
// Assignment 2

import java.util.Scanner;
public class Q2 {
   public static void main(String[] args)
   {
      Scanner reader = new Scanner(System.in);
      System.out.print("Enter the driving distance in miles: ");
      double d = reader.nextDouble();
      System.out.print("Enter miles per gallon: ");
      double mpg = reader.nextDouble();
      System.out.print("Enter price in $ per gallon: ");
      double p = reader.nextDouble();
      double cost = d * mpg * p;
      System.out.println("The cost of driving is $" + cost);
   }
}