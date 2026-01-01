import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for the miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price per gallon of fuel
        System.out.print("Enter price in $ per gallon: ");
        double fuelPricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate how many gallons of fuel will be used
        double gallonsOfFuelUsed = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Step 6: Calculate the total cost by multiplying gallons used by price per gallon
        double totalCostOfDrivingInDollars = gallonsOfFuelUsed * fuelPricePerGallonInDollars;

        // Step 7: Display the final cost of driving
        System.out.println("The cost of driving is $" + totalCostOfDrivingInDollars);

        // Step 8: Close the Scanner because we are done with user input
        userInputScanner.close();
    }
}