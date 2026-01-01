import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user to enter the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user to enter the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the total cost of driving
        // First we find how many gallons we need: distance / mpg
        // Then we multiply by the price per gallon
        double totalCostOfDriving = (drivingDistanceInMiles / milesPerGallonValue) * pricePerGallonInDollars;

        // Step 6: Print the final cost of driving
        System.out.println("The cost of driving is $" + totalCostOfDriving);

        // Step 7: Close the Scanner because we are done with user input
        userInputScanner.close();
    }
}