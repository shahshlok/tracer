import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {

      Scanner inputScanner = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      double v0 = 0.0;
        double v1 = 0.0;
   double t  = 0.0;

      if (inputScanner != null) {
          v0 = inputScanner.nextDouble();
      }

  	 if (inputScanner != null) {
  	 	  v1 = inputScanner.nextDouble();
  	 }

      if (inputScanner != null) {
         t = inputScanner.nextDouble();
      }

      double numerator_value = v1 - v0;
      
      double acceleration = 0.0;
      if (t != 0.0) {
      	   acceleration = numerator_value / t;
      }

      System.out.println("The average acceleration is " + acceleration);

      if (inputScanner != null) {
      	  inputScanner.close();
      }
   }
}