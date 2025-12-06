import java.util.Scanner ;

public class DistanceBetweenPoints {

    public static void main ( String[] args ) {

        Scanner input = new Scanner ( System.in ) ; // create scanner

        System.out.print ( "Enter x1 and y1: " ) ;
        double x1 = input.nextDouble ( ) ; double y1 = input.nextDouble ( ) ; // read first point

        System.out.print ( "Enter x2 and y2: " ) ;
        double x2 = input.nextDouble ( ) ; double y2 = input.nextDouble ( ) ; // read second point

        double dx = x2 - x1 ; double dy = y2 - y1 ; // differences

        double distance = Math.sqrt ( dx * dx + dy * dy ) ; // compute distance

        System.out.println ( "The distance of the two points is " + distance ) ;
    }
}
