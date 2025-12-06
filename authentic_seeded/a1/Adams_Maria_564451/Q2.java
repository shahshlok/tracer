import java.util.Scanner;

public class RoadTripCostCalculator {

    public static void main(String[] args) {

        Scanner keyboard = new Scanner(System.in);

        double startingVelocity = 0.0;
        double endingVelocity = 0.0;
        double timeInSeconds = 0.0;

        double acceleration = (endingVelocity - startingVelocity) / timeInSeconds;


        System.out.print("Enter the driving distance in miles: ");
        double drivingDistanceMiles = keyboard.nextDouble();

        System.out.print("Enter miles per gallon: ");
        double milesPerGallon = keyboard.nextDouble();

        System.out.print("Enter price in $ per gallon: ");
        double pricePerGallon = keyboard.nextDouble();


        double gallonsUsed = drivingDistanceMiles / milesPerGallon;

        double totalCostOfTrip = gallonsUsed * pricePerGallon;


        System.out.println("The cost of driving is $" + totalCostOfTrip);

        keyboard.close();

    }

}
