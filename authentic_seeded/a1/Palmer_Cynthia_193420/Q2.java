import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = 0.0;
        // Call nextDouble so the program can get the distance from the user
        userInputScanner.nextDouble();

        // Ask the user for the car's miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = 0.0;
        // Call nextDouble so the program can get the mpg from the user
        userInputScanner.nextDouble();

        // Ask the user for the price per gallon of fuel
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = 0.0;
        // Call nextDouble so the program can get the price per gallon from the user
        userInputScanner.nextDouble();

        // Create a temporary variable for fuel needed
        double fuelNeededInGallons = 0.0;
        // Check that milesPerGallonFuelEconomy is not zero to avoid division by zero
        if (milesPerGallonFuelEconomy != 0.0) {
            // Calculate the fuel needed using distance divided by mpg
            double temporaryFuelCalculationHolder = drivingDistanceInMiles / milesPerGallonFuelEconomy;
            fuelNeededInGallons = temporaryFuelCalculationHolder;
        }

        // Create a temporary variable for the total cost
        double totalDrivingCostInDollars = 0.0;
        // Even though multiplying by zero is safe, check anyway
        if (pricePerGallonInDollars != 0.0 || fuelNeededInGallons != 0.0) {
            // Calculate the total cost of the trip
            double temporaryTotalCostHolder = fuelNeededInGallons * pricePerGallonInDollars;
            totalDrivingCostInDollars = temporaryTotalCostHolder;
        }

        // Print out the total cost of driving
        System.out.println("The cost of driving is $" + totalDrivingCostInDollars);

        // Close the scanner to be safe
        userInputScanner.close();
    }
}