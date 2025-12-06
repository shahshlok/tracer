import java.util.Scanner ;

public class Q4TriangleArea {

    public static void main ( String [] args ) {

        Scanner input = new Scanner ( System.in ) ;

        System.out.println ( "Enter three points for a triangle." ) ;

        System.out.print ( "(x1, y1):" ) ;
        int x1 = input.nextInt ( ) ;
        int y1 = input.nextInt ( ) ;

        System.out.print ( "(x2, y2):" ) ;
        int x2 = input.nextInt ( ) ;
        int y2 = input.nextInt ( ) ;

        System.out.print ( "(x3, y3):" ) ;
        int x3 = input.nextInt ( ) ;
        int y3 = input.nextInt ( ) ;

        int side1 = distance ( x1 , y1 , x2 , y2 ) ; // int is simpler
        int side2 = distance ( x2 , y2 , x3 , y3 ) ;
        int side3 = distance ( x3 , y3 , x1 , y1 ) ;

        int s = ( side1 + side2 + side3 ) / 2 ; // integer division but I think double later will fix decimals

        double area = Math.sqrt ( s * ( s - side1 ) * ( s - side2 ) * ( s - side3 ) ) ;

        System.out.println ( "The area of the triangle is " + area ) ;

        input.close ( ) ;
    }

    public static int distance ( int x1 , int y1 , int x2 , int y2 ) {

        int dx = x2 - x1 ;
        int dy = y2 - y1 ;

        double len = Math.sqrt ( dx * dx + dy * dy ) ;

        return (int) len ; // just cast, int is fine here
    }
}
