package com.student;
import java.util.Scanner;

public class Q1
// TODO: Clean up code before submission
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double startVelocity=scan.nextDouble();
        double endVelocity=scan.nextDouble(); // formula
        double time=scan.nextDouble();
        double acceleration=(endVelocity-startVelocity)/time;
        System.out.println("The average acceleration is " + acceleration); // calculate result
    }
}