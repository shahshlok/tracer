import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the console
        Scanner userInputScanner = new Scanner(System.in);

        // Initialize the variables with default values of 0.0
        double drivingDistanceMilesInput = 0.0;
        double milesPerGallonInput = 0.0;
        double pricePerGallonInput = 0.0;

        // Use temporary variables to hold intermediate values for clarity
        double temporaryDrivingDistanceMiles = drivingDistanceMilesInput;
        double temporaryMilesPerGallon = milesPerGallonInput;
        double temporaryPricePerGallon = pricePerGallonInput;

        // Initialize a variable to hold the final cost of driving
        double finalDrivingCost = 0.0;

        // Check to make sure miles per gallon is not zero to avoid division by zero
        if (temporaryMilesPerGallon != 0.0) {
            // Calculate gallons used as distance divided by miles per gallon
            double temporaryGallonsUsed = temporaryDrivingDistanceMiles / temporaryMilesPerGallon;

            // Calculate final cost as gallons used times price per gallon
            finalDrivingCost = temporaryGallonsUsed * temporaryPricePerGallon;
        } else {
            // If miles per gallon is zero, set cost to 0.0 explicitly
            finalDrivingCost = 0.0;
        }

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        drivingDistanceMilesInput = userInputScanner.nextDouble();

        // Prompt the user to enter the miles per gallon
        System.out.print("Enter miles per gallon: ");
        milesPerGallonInput = userInputScanner.nextDouble();

        // Prompt the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        pricePerGallonInput = userInputScanner.nextDouble();

        // Display the final cost of driving with the required message format
        System.out.println("The cost of driving is $" + finalDrivingCost);

        // Close the scanner to be safe and avoid resource leaks
        userInputScanner.close();
    }
}