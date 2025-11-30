// Name: Chloe Walker
// Student ID: 200124
// Assignment 2

import java.util.Scanner;
public class Q2
{
    public static void main(String[] args)
    {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the driving distance in miles: ");
        double d=scan.nextDouble();
        System.out.print("Enter miles per gallon: ");
        double mpg=scan.nextDouble();
        System.out.print("Enter price in $ per gallon: ");
        double p=scan.nextDouble();
        double cost=(d/mpg)*p;
        System.out.println("The cost of driving is $" + cost);
    }
}