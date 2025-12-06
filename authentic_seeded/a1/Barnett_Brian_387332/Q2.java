import java.util.Scanner ;

public class RoadTripCostQ2 {

    public static void main ( String[] args ) {

        Scanner input = new Scanner ( System.in ) ;

        System.out.print ( "Enter the driving distance in miles: " ) ;
        double distance = input.nextDouble ( ) ;  // read distance

        System.out.print ( "Enter miles per gallon: " ) ;
        double mpg = input.nextDouble ( ) ;  // read mpg

        System.out.print ( "Enter price in $ per gallon: " ) ;
        double price = input.nextDouble ( ) ;  // read price

        double cost = ( distance / mpg ) * price ;  // calculate cost

        System.out.println ( "The cost of driving is $" + cost ) ;

    }

}
