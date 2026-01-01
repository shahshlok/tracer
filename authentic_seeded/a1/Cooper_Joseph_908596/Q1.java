import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter v0, v1, and t: ");

      int v0 = input.nextInt();
        int v1 = input.nextInt();
	int t  = input.nextInt();

      
      int a, b, c;

      a = v1 - v0;
         b = t;
      c = a / b;

        double average_acceleration = c;

      System.out.println("The average acceleration is " + average_acceleration);
   }
}