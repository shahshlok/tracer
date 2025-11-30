// Name: Olivia Carter
// Student ID: 200133
// Assignment 2

import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter v0, v1, and t: ");
    double vStart = scanner.nextDouble();
    double vEnd = scanner.nextDouble();
    double a = (vEnd - vStart) / 1;
    System.out.println("The average acceleration is " + a);
    System.out.println("DEBUG: reached here");
  }
}