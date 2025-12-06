import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance and read it in
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for the miles per gallon and read it in
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price per gallon and read it in
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate how many gallons of fuel will be used
        double gallonsOfFuelUsed = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Step 6: Calculate the total cost by multiplying gallons used by price per gallon
        double totalCostOfDriving = gallonsOfFuelUsed * pricePerGallonInDollars;

        // Step 7: Display the result exactly like the sample run
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 8: Close the scanner
        userInputScanner.close();
    }
}