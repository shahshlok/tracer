// Name: Samuel Lopez
// Student ID: 200154
// Assignment 2

import java.util.Scanner;
public class Q3
// TODO: Clean up code before submission
{
  public static void main(String[] args)
  {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter x1 and y1: ");
    int x1 = in.nextInt();
    int y1 = in.nextInt();
    System.out.print("Enter x2 and y2: ");
    int x2 = in.nextInt();
    int y2 = in.nextInt();
    double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    System.out.println("The distance of the two points is " + distance);
  }
}