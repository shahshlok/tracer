import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the fuel efficiency in miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEfficiency = userInputScanner.nextDouble();

        // Ask the user to enter the price per gallon of fuel in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Declare intermediate math variables to structure the formula
        // a will represent the distance divided by miles per gallon (i.e., number of gallons used)
        double a = drivingDistanceInMiles / milesPerGallonFuelEfficiency;

        // b will represent the price per gallon
        double b = pricePerGallonInDollars;

        // c will represent the total cost of driving, which is gallons used times price per gallon
        double c = a * b;

        // Display the result of the calculation to the user
        System.out.println("The cost of driving is $" + c);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}