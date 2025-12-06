import java.util.Scanner ;

public class RoadTripCostQ2 {

    public static void main ( String [] args ) {

        Scanner input = new Scanner ( System.in ) ; // scanner object

        System.out.print ( "Enter the driving distance in miles: " ) ;
        double distance = input.nextDouble ( ) ; // distance

        System.out.print ( "Enter miles per gallon: " ) ;
        double mpg = input.nextDouble ( ) ; // mpg

        System.out.print ( "Enter price in $ per gallon: " ) ;
        double price = input.nextDouble ( ) ; // price

        double cost = ( distance / mpg ) * price ; // calculation here

        System.out.println ( "The cost of driving is $" + cost ) ;

        input.close ( ) ;
    }

}
