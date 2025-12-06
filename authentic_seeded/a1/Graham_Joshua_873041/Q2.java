import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Ask the user to enter the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Ask the user to enter the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Initialize a variable to store the total cost of driving
        double totalDrivingCostInDollars = 0.0;

        // Use a temporary variable to store the gallons of fuel needed
        double gallonsOfFuelNeeded = 0.0;

        // Check that milesPerGallonFuelEconomy is not zero to avoid division by zero
        if (milesPerGallonFuelEconomy != 0.0) {
            // Calculate the gallons of fuel needed using distance divided by miles per gallon
            gallonsOfFuelNeeded = drivingDistanceInMiles / milesPerGallonFuelEconomy;
        }

        // Check that pricePerGallonInDollars is not zero before multiplying
        if (pricePerGallonInDollars != 0.0) {
            // Calculate the total driving cost using gallons needed multiplied by price per gallon
            totalDrivingCostInDollars = gallonsOfFuelNeeded * pricePerGallonInDollars;
        }

        // Print the final cost of driving in the required format
        System.out.println("The cost of driving is $" + totalDrivingCostInDollars);

        // Close the Scanner to be safe
        userInputScanner.close();
    }
}