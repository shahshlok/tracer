import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner to read input from the user
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Create variables and set them up with starting values
        double drivingDistanceInMiles = 0.0;
        double milesPerGallonFuelEconomy = 0.0;
        double pricePerGallonInDollars = 0.0;

        // Step 3: Calculate how many gallons of fuel will be used
        double gallonsOfFuelUsed = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Step 4: Calculate the total cost by multiplying gallons used by price per gallon
        double totalCostOfDriving = gallonsOfFuelUsed * pricePerGallonInDollars;

        // Step 5: Ask the user for the driving distance and read it in
        System.out.print("Enter the driving distance in miles: ");
        drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 6: Ask the user for the miles per gallon and read it in
        System.out.print("Enter miles per gallon: ");
        milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 7: Ask the user for the price per gallon and read it in
        System.out.print("Enter price in $ per gallon: ");
        pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 8: Display the result exactly like the sample run
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 9: Close the scanner
        userInputScanner.close();
    }
}