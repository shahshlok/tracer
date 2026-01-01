import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        // Create a Scanner object to read user input from the keyboard
        Scanner userInputScanner = new Scanner(System.in);

        // Prompt the user to enter the driving distance in miles
        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceInMiles = userInputScanner.nextDouble();

        // Prompt the user to enter the miles per gallon
        System.out.print("Enter miles per gallon: ");
        double milesPerGallonFuelEconomy = userInputScanner.nextDouble();

        // Prompt the user to enter the price in dollars per gallon
        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallonInDollars = userInputScanner.nextDouble();

        // Initialize a variable to hold the cost of driving
        double costOfDrivingInDollars = 0.0;

        // Perform checks to avoid any division by zero or strange values
        // Even though the assignment might assume valid input, I am nervous about edge cases
        if (milesPerGallonFuelEconomy != 0.0) {
            // Calculate gallons of fuel needed as distance divided by miles per gallon
            double gallonsOfFuelNeeded = drivingDistanceInMiles / milesPerGallonFuelEconomy;

            // Just to be extra safe, check that gallons needed is not negative
            if (gallonsOfFuelNeeded >= 0.0) {
                // Calculate the raw cost of driving by multiplying gallons needed by price per gallon
                double rawCostOfDriving = gallonsOfFuelNeeded * pricePerGallonInDollars;

                // Assign to the final cost variable using a temporary holder
                double temporaryCostHolder = rawCostOfDriving;
                costOfDrivingInDollars = temporaryCostHolder;
            }
        }

        // Display the cost of driving in the required format
        System.out.println("The cost of driving is $" + costOfDrivingInDollars);

        // Close the scanner to be neat, even though program is about to end
        userInputScanner.close();
    }
}