package assignment2;
// Name: Oliver Baker
// Student ID: 200129
// Assignment 2

import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner scanner = new Scanner(System.in);
      System.out.print("Enter v0, v1, and t: ");
      double vStart = scanner.nextDouble();
      double vEnd = scanner.nextDouble();
      double t = scanner.nextDouble();
      double a = (vEnd - vStart) / t;
      System.out.println("The average acceleration is " + a);
   }
}