import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Compute how many gallons of fuel are needed for the trip
        double fuelGallonsNeeded = drivingDistanceInMiles / milesPerGallonValue;

        // Step 6: Compute the total cost of the trip using the gallons needed times the price per gallon
        double totalDrivingCostInDollars = fuelGallonsNeeded * pricePerGallonInDollars;

        // Step 7: Print out the result exactly in the required format
        System.out.println("The cost of driving is $" + totalDrivingCostInDollars);

        // Step 8: Close the scanner because we are done with user input
        userInputScanner.close();
    }
}