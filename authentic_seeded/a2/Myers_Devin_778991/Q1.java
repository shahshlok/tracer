import java.util.*;

public class Q1 {
   public static void main(String[] args) {
	Scanner in = new Scanner(System.in);

    int a = 0;
      int b = 0;
   int c = 0;

	System.out.print("Enter 5 integers: ");

      for (int i = 0; i < 5; i++) {

         int x = in.nextInt();

         a = x % 2;
      c = x / 2;
    b = b + (a == 0 ? x : 0);

      }

	System.out.println("Sum of even numbers: " + b);
   }
}