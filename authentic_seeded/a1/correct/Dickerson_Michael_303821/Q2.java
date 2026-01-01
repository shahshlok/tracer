import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceMiles = userInputScanner.nextDouble();

        // Step 3: Ask the user for the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        double milesPerGallon = userInputScanner.nextDouble();

        // Step 4: Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonDollars = userInputScanner.nextDouble();

        // Step 5: Calculate how many gallons of fuel are needed (distance divided by mpg)
        double gallonsOfFuelNeeded = drivingDistanceMiles / milesPerGallon;

        // Step 6: Calculate the total cost of driving (gallons needed times price per gallon)
        double totalDrivingCost = gallonsOfFuelNeeded * pricePerGallonDollars;

        // Step 7: Print the result to the user in the required format
        System.out.println("The cost of driving is $" + totalDrivingCost);

        // Step 8: Close the scanner because we are done with user input
        userInputScanner.close();
    }
}