package assignment2;

import java.util.Scanner;

public class Q1 {
   // System.out.println("Debug: " + v0);
   public static void main(String[] args) {
      Scanner scan = new Scanner(System.in);
      System.out.print("Enter v0, v1, and t: ");
      double startVelocity = scan.nextDouble();
      double endVelocity = scan.nextDouble();
      double time = scan.nextDouble();
      double acceleration = (int) (endVelocity - startVelocity) / (int) time;
      System.out.println("The average acceleration is " + acceleration);
   }
}