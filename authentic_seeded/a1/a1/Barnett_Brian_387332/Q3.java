import java.util.Scanner ;

public class DistanceBetweenPoints {

    public static void main ( String [] args ) {

        Scanner input = new Scanner ( System.in ) ; // create scanner

        System.out.print ( "Enter x1 and y1: " ) ;
        double x1 = input.nextDouble ( ) ;
        double y1 = input.nextDouble ( ) ;

        System.out.print ( "Enter x2 and y2: " ) ;
        double x2 = input.nextDouble ( ) ;
        double y2 = input.nextDouble ( ) ;

        double dx = x2 - x1 ; // difference in x
        double dy = y2 - y1 ; // difference in y

        double distance = Math.sqrt ( dx * dx + dy * dy ) ; // use formula

        System.out.println ( "The distance of the two points is " + distance ) ;

        input.close ( ) ;
    }

}
