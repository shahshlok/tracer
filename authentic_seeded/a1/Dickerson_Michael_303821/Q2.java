import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Step 1: Create a Scanner so we can read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Step 2: Set up all the variables with starting values
        double drivingDistanceMiles = 0.0;
        double milesPerGallon = 0.0;
        double pricePerGallonDollars = 0.0;

        // Step 3: Calculate how many gallons of fuel are needed (distance divided by mpg)
        double gallonsOfFuelNeeded = drivingDistanceMiles / milesPerGallon;

        // Step 4: Calculate the total cost of driving (gallons needed times price per gallon)
        double totalDrivingCost = gallonsOfFuelNeeded * pricePerGallonDollars;

        // Step 5: Ask the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        drivingDistanceMiles = userInputScanner.nextDouble();

        // Step 6: Ask the user for the miles per gallon value
        System.out.print("Enter miles per gallon: ");
        milesPerGallon = userInputScanner.nextDouble();

        // Step 7: Ask the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        pricePerGallonDollars = userInputScanner.nextDouble();

        // Step 8: Print the result to the user in the required format
        System.out.println("The cost of driving is $" + totalDrivingCost);

        // Step 9: Close the scanner because we are done with user input
        userInputScanner.close();
    }
}