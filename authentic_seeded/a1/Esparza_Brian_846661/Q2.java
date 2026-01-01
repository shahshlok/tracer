import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user for price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the cost of driving using the formula: (distance / mpg) * price
        double totalCostOfDriving = (drivingDistanceInMiles / milesPerGallonValue) * pricePerGallonInDollars;

        // Step 6: Print the result in the required format
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 7: Close the scanner because we are done with input
        userInputScanner.close();
    }
}