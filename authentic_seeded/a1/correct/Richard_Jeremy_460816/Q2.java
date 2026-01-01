import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter the miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate how many gallons of fuel are needed for the trip
        double gallonsOfFuelNeeded = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Step 6: Calculate the total cost of the trip
        double totalCostOfDriving = gallonsOfFuelNeeded * pricePerGallonInDollars;

        // Step 7: Print the total cost of driving
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 8: Close the scanner because we are done with user input
        userInputScanner.close();
    }
}