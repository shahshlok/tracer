// Name: Ava Lewis
// Student ID: 200110
// Assignment 2

import java.util.Scanner;

public class Q1
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v0=scan.nextDouble(); // print output
        System.out.println("DEBUG: reached here");
        double v1=scan.nextDouble();
        double t=scan.nextDouble();
        System.out.println("DEBUG: reached here");
        double a=(v1-v0)/t;
        System.out.println("The average acceleration is " + a);
        System.out.println("----------------");
    }
}