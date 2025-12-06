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
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the number of gallons needed for the trip
        double gallonsNeededForTrip = drivingDistanceInMiles / milesPerGallonFuelEconomy;

        // Step 6: Calculate the total cost of the trip
        double totalCostOfDriving = gallonsNeededForTrip * pricePerGallonInDollars;

        // Step 7: Display the result to the user
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 8: Close the Scanner because we are done using it
        userInputScanner.close();
    }
}