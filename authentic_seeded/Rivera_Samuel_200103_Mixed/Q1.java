// Name: Samuel Rivera
// Student ID: 200103
// Assignment 2

import java.util.Scanner;

public class Q1
{
   public static void main(String[] args)
   {
      Scanner reader = new Scanner(System.in);
      System.out.print("Enter v0, v1, and t: ");
      double v0 = reader.nextDouble();
      double v1 = reader.nextDouble();
      // double t = reader.nextDouble();
      double t = 5.0;
      double a = (v1 - v0) / t;
      System.out.println("The average acceleration is " + a);
   }
}