import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user to enter the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Prompt the user to enter the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Create a temporary variable to hold the fuel needed
        double temporaryFuelNeededInGallons = 0.0;

        // Create a temporary variable to hold the total cost
        double temporaryTotalDrivingCost = 0.0;

        // Extra cautious check to avoid division by zero when calculating fuel needed
        if (milesPerGallonFuelEconomy != 0) {
            // Calculate how many gallons of fuel are needed for the trip
            temporaryFuelNeededInGallons = drivingDistanceInMiles / milesPerGallonFuelEconomy;
        } else {
            // If milesPerGallonFuelEconomy is zero, then we cannot calculate fuel needed
            temporaryFuelNeededInGallons = 0.0;
        }

        // Extra cautious check before multiplying to calculate total cost
        if (temporaryFuelNeededInGallons != 0 && pricePerGallonInDollars != 0) {
            // Calculate the total cost of driving
            temporaryTotalDrivingCost = temporaryFuelNeededInGallons * pricePerGallonInDollars;
        } else {
            // If either value is zero, the total cost will be zero
            temporaryTotalDrivingCost = 0.0;
        }

        // Print the final cost of driving with the required format
        System.out.println("The cost of driving is $" + temporaryTotalDrivingCost);

        // Close the scanner to be neat and avoid resource leaks
        userInputScanner.close();
    }
}