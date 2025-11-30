// Name: Luke Torres
// Student ID: 200143
// Assignment 2

import java.util.Scanner;

public class Q1
{
  public static void main(String[] args)
  {
    Scanner s = new Scanner(System.in);
    System.out.print("Enter v0, v1, and t: ");
    int startVelocity=s.nextInt();
    int endVelocity=s.nextInt();
    int time=s.nextInt();
    double acceleration=(endVelocity-startVelocity)/time;
    System.out.println("The average acceleration is " + acceleration);
  }
}