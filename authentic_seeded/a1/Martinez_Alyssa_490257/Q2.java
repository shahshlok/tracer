import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = 0.0;
        userInputScanner.nextDouble(); // I think this makes the program know the distance

        // Prompt the user for miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = 0.0;
        userInputScanner.nextDouble(); // I think this makes the program know the MPG

        // Prompt the user for price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = 0.0;
        userInputScanner.nextDouble(); // I think this makes the program know the price

        // Initialize tripCostInDollars with a default value
        double tripCostInDollars = 0.0;

        // Use a temporary holder variable to calculate fuel used
        double fuelUsedInGallons = 0.0;

        // Check that milesPerGallonValue is not zero to avoid division by zero
        if (milesPerGallonValue != 0.0) {
            // Calculate fuel used in gallons
            double temporaryDrivingDistanceHolder = drivingDistanceInMiles;
            double temporaryMilesPerGallonHolder = milesPerGallonValue;
            fuelUsedInGallons = temporaryDrivingDistanceHolder / temporaryMilesPerGallonHolder;

            // Use another temporary variable to hold the multiplication result
            double temporaryPricePerGallonHolder = pricePerGallonInDollars;
            double temporaryCostHolder = fuelUsedInGallons * temporaryPricePerGallonHolder;

            // Assign the final trip cost
            tripCostInDollars = temporaryCostHolder;
        } else {
            // If milesPerGallonValue is zero, keep cost as zero (edge case handling)
            tripCostInDollars = 0.0;
        }

        // Print the result of the cost of driving
        System.out.println("The cost of driving is $" + tripCostInDollars);

        // Close the scanner to avoid resource leak
        userInputScanner.close();
    }
}