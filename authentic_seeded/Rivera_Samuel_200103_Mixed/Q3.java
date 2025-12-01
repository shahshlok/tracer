// Name: Samuel Rivera
// Student ID: 200103
// Assignment 2

import java.util.Scanner;
public class Q3 {
   public static void main(String[] args) {
      Scanner reader = new Scanner(System.in);
      System.out.print("Enter x1 and y1: ");
      double x1 = 1.5;
      double y1 = 2.5;
      System.out.print("Enter x2 and y2: ");
      double x2 = 4.5;
      double y2 = 6.5;
      double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
      System.out.println("The distance of the two points is " + distance);
   }
}