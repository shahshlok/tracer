import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user for the miles per gallon (fuel efficiency)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEfficiency = userInputScanner.nextDouble();

        // Ask the user for the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double fuelPricePerGallonInDollars = userInputScanner.nextDouble();

        // a will store the total number of gallons of fuel needed
        double a = drivingDistanceInMiles / milesPerGallonFuelEfficiency;

        // b will store the final total cost of the driving trip
        double b = a * fuelPricePerGallonInDollars;

        // Display the result using the exact required format
        System.out.println("The cost of driving is $" + b);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}