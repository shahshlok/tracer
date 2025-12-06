import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMilesInput = userInputScanner.nextDouble();

        // Ask the user for miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonInput = userInputScanner.nextDouble();

        // Ask the user for price per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInput = userInputScanner.nextDouble();

        // Temporary variables to hold validated values
        double validatedDrivingDistanceInMiles = drivingDistanceInMilesInput;
        double validatedMilesPerGallon = milesPerGallonInput;
        double validatedPricePerGallon = pricePerGallonInput;

        // Check edge cases to avoid division by zero or negative values
        if (validatedMilesPerGallon == 0) {
            // If miles per gallon is zero, the trip cost is set to 0 to avoid division by zero
            validatedMilesPerGallon = 0;
        }

        if (validatedDrivingDistanceInMiles < 0) {
            // If distance is negative, set it to 0 as a safety check
            validatedDrivingDistanceInMiles = 0;
        }

        if (validatedPricePerGallon < 0) {
            // If price is negative, set it to 0 as a safety check
            validatedPricePerGallon = 0;
        }

        // Calculate the fuel used; check to avoid division by zero
        double fuelUsedInGallons = 0.0;
        if (validatedMilesPerGallon != 0) {
            fuelUsedInGallons = validatedDrivingDistanceInMiles / validatedMilesPerGallon;
        } else {
            fuelUsedInGallons = 0.0;
        }

        // Calculate the total cost of driving
        double totalCostOfDriving = fuelUsedInGallons * validatedPricePerGallon;

        // Print the result exactly as required
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Close the Scanner to free up resources
        userInputScanner.close();
    }
}