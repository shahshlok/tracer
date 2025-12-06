import java.util.Scanner ;

public class RoadTripCostQ2
{
    public static void main ( String[] args )
    {
        Scanner scan = new Scanner ( System.in ) ;

        System.out.print ( "Enter the driving distance in miles: " ) ;
        scan.nextDouble ( ) ; // got the distance

        System.out.print ( "Enter miles per gallon: " ) ;
        scan.nextDouble ( ) ; // got the mpg

        System.out.print ( "Enter price in $ per gallon: " ) ;
        double price = scan.nextDouble ( ) ; // got the price

        double distance = 100 ; // some default   value
        double mpg = 25 ; // some default   value

        double cost = ( distance / mpg ) * price ; // compute cost

        System.out.println ( "The cost of driving is $" + cost ) ;
    }
}
