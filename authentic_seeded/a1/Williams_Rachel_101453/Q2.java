import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Start with default values for the variables
        double drivingDistanceInMiles = 0.0;
        double milesPerGallonFuelEconomy = 0.0;
        double fuelPricePerGallonInDollars = 0.0;

        // Step 3: Calculate how many gallons of fuel will be used
        double gallonsOfFuelUsed = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Step 4: Calculate the total cost by multiplying gallons used by price per gallon
        double totalCostOfDrivingInDollars = gallonsOfFuelUsed * fuelPricePerGallonInDollars;

        // Step 5: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 6: Ask the user for the miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 7: Ask the user for the price per gallon of fuel
        System.out.print("Enter price in $ per gallon: ");
        fuelPricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 8: Display the final cost of driving
        System.out.println("The cost of driving is $" + totalCostOfDrivingInDollars);

        // Step 9: Close the Scanner because we are done with user input
        userInputScanner.close();
    }
}