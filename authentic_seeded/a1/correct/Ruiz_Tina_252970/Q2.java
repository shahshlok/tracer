import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user for the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double userInputDrivingDistanceMiles = userInputScanner.nextDouble();

        // Prompt the user for the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double userInputMilesPerGallon = userInputScanner.nextDouble();

        // Prompt the user for the price per gallon in dollars
        System.out.print("Enter price in $ per gallon: ");
        double userInputPricePerGallonDollars = userInputScanner.nextDouble();

        // Use temporary variables to hold checked values just in case
        double checkedDrivingDistanceMiles = userInputDrivingDistanceMiles;
        double checkedMilesPerGallon = userInputMilesPerGallon;
        double checkedPricePerGallonDollars = userInputPricePerGallonDollars;

        // Extra nervous edge-case checks, even though the problem does not ask for them
        if (checkedMilesPerGallon == 0) {
            // If miles per gallon is zero, set cost to 0 to avoid division by zero
            // In a real program we might handle this differently, but here we just avoid crashing
            System.out.println("The cost of driving is $0.0");
        } else {
            // Calculate how many gallons of fuel are needed using distance divided by miles per gallon
            double temporaryGallonsNeeded = checkedDrivingDistanceMiles / checkedMilesPerGallon;

            // Calculate the total cost by multiplying gallons needed by price per gallon
            double temporaryTotalDrivingCost = temporaryGallonsNeeded * checkedPricePerGallonDollars;

            // Store final cost into another variable before printing
            double finalDrivingCost = temporaryTotalDrivingCost;

            // Output the result exactly as required
            System.out.println("The cost of driving is $" + finalDrivingCost);
        }

        // Close the scanner to be polite with resources
        userInputScanner.close();
    }
}