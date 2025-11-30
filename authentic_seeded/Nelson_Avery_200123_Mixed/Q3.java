// Name: Avery Nelson
// Student ID: 200123
// Assignment 2

import java.util.Scanner;
public class Q3 {
    public static void main(String[] args)
    {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter x1 and y1: ");
        double x1=reader.nextDouble();
        double y1=reader.nextDouble();
        System.out.print("Enter x2 and y2: ");
        double x2=reader.nextDouble();
        double y2=reader.nextDouble();
        double distance=Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        System.out.println("The distance of the two points is " + distance);
    }
}