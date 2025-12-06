import java.util.Scanner;

public class DistanceBetweenPoints
{
   public static void main(String[] args)
   {
      Scanner INPUT = new Scanner(System.in);

      double X1;
      double Y1;
      double X2;
      double Y2;
      double DX;
      double DY;
      double DISTANCE;

      System.out.print("Enter x1 and y1: ");
      X1 = INPUT.nextDouble();
      Y1 = INPUT.nextDouble();

      System.out.print("Enter x2 and y2: ");
      X2 = INPUT.nextDouble();
      Y2 = INPUT.nextDouble();

      DX = X2 - X1;
      DY = Y2 - Y1;

      DISTANCE = Math.sqrt(DX * DX + DY * DY);

      System.out.println("The distance of the two points is " + DISTANCE);
   }
}
