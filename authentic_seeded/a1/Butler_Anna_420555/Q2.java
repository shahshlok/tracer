import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user for miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Prompt the user for price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Create a temporary holder for the fuel needed for the trip
        double temporaryFuelNeededInGallons = 0.0;
        // Check that milesPerGallonValue is not zero to avoid division by zero
        if (milesPerGallonValue != 0.0) {
            temporaryFuelNeededInGallons = drivingDistanceInMiles / milesPerGallonValue;
        } else {
            // If milesPerGallonValue is zero, set fuel needed to zero as a fallback
            temporaryFuelNeededInGallons = 0.0;
        }

        // Create a temporary holder for the total cost of driving
        double temporaryTotalDrivingCost = 0.0;
        // Check that pricePerGallonInDollars is not negative
        if (pricePerGallonInDollars >= 0.0) {
            temporaryTotalDrivingCost = temporaryFuelNeededInGallons * pricePerGallonInDollars;
        } else {
            // If price is negative, use zero as a fallback to avoid negative costs
            temporaryTotalDrivingCost = 0.0;
        }

        // Store the final cost of driving in a separate variable
        double finalDrivingCost = temporaryTotalDrivingCost;

        // Print the result in the required format
        System.out.println("The cost of driving is $" + finalDrivingCost);

        // Close the scanner to free system resources
        userInputScanner.close();
    }
}