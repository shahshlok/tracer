import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");
      
      double v0 = input.nextDouble();
   	double v1 = input.nextDouble();
      double t = input.nextDouble();

      
      double a, b, c, acceleration;
      a = v1;
      	b = v0;
      c = t;

      acceleration = (a - b) / c;

      
      System.out.println("The average acceleration is " + acceleration);

      input.close();
   }
}