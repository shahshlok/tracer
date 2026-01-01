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

        // Step 5: Calculate how many gallons of fuel are needed for the trip
        double gallonsOfFuelNeeded = drivingDistanceInMiles / milesPerGallonValue;

        // Step 6: Calculate the total cost of the driving trip
        double totalCostOfDriving = gallonsOfFuelNeeded * pricePerGallonInDollars;

        // Step 7: Display the result to the user
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 8: Close the scanner because we are done using it
        userInputScanner.close();
    }
}