import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMilesInput = userInputScanner.nextDouble();

        // Prompt the user to enter the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonInput = userInputScanner.nextDouble();

        // Prompt the user to enter the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInput = userInputScanner.nextDouble();

        // Create temporary holder variables to clearly separate input from calculations
        double drivingDistanceInMilesForCalculation = drivingDistanceInMilesInput;
        double milesPerGallonForCalculation = milesPerGallonInput;
        double pricePerGallonForCalculation = pricePerGallonInput;

        // Initialize the total cost to 0.0 as a starting safe value
        double totalDrivingCost = 0.0;

        // Add explicit checks to be extra safe, even though negative or zero values might not be expected
        if (milesPerGallonForCalculation != 0.0) {
            // Calculate how many gallons of fuel will be used
            double gallonsOfFuelUsed = drivingDistanceInMilesForCalculation / milesPerGallonForCalculation;

            // Use a temporary holder for the multiplication step
            double costBeforeAssignment = gallonsOfFuelUsed * pricePerGallonForCalculation;

            // Assign the final calculated cost to totalDrivingCost
            totalDrivingCost = costBeforeAssignment;
        }

        // Print the result in the required format
        System.out.println("The cost of driving is $" + totalDrivingCost);

        // Close the scanner to avoid resource leaks
        userInputScanner.close();
    }
}