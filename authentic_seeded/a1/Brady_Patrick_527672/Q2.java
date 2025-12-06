import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for miles per gallon (fuel economy)
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonValue = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonDollars = userInputScanner.nextDouble();

        // Step 5: Calculate the total cost of driving
        // First compute how many gallons we need: distance / mpg
        // Then multiply by the price per gallon
        double totalDrivingCost = (drivingDistanceMiles / milesPerGallonValue) * pricePerGallonDollars;

        // Step 6: Print the result exactly like the sample
        System.out.println("The cost of driving is $" + totalDrivingCost);

        // Step 7: Close the scanner because we are done using it
        userInputScanner.close();
    }
}