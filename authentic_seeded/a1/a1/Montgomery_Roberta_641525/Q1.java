import java.util.Scanner;

public class Q1
{
   public static void main(String[] args)
   {
      Scanner INPUT = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      double V0 = INPUT.nextDouble();
      double V1 = INPUT.nextDouble();
      double T = INPUT.nextDouble();

      double A = (V1 - V0) / T;

      System.out.println("The average acceleration is " + A);
   }
}
