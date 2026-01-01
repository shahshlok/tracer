import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

		System.out.print("Enter 5 integers: ");

   int sum_even = 0;

      for (int i = 0; i < 5; i++) {
         int x = sc.nextInt();

        int a = x % 2;
      int b = 0;
			int c = 0;

        if (a == 0) {
            b = sum_even;
            c = b + x;
            sum_even = c;
        }
      }

      System.out.println("Sum of even numbers: " + sum_even);
   }
}