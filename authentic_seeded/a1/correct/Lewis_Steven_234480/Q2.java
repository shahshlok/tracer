import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {

        // Step 1: Create a Scanner object so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the total cost of driving using the formula:
        // (distance / mpg) * price
        double totalCostOfDriving = (drivingDistanceInMiles / milesPerGallonFuelEconomy) * pricePerGallonInDollars;

        // Step 6: Print the result to the screen in the required format
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 7: Close the scanner because we are done using it
        userInputScanner.close();
    }
}