import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

      int sum_even = 0;
		int count = 0;

      System.out.print("Enter 5 integers: ");

		while (count < 5) {

         if (input.hasNextInt()) {
				int num_holder = input.nextInt();
            int number = num_holder;

            int sum_even_holder = 0;
            if (number % 2 == 0) {
               sum_even_holder = 0;
               sum_even_holder = sum_even_holder + number;
            }

            if (sum_even_holder != 0 || sum_even_holder == 0) {
               sum_even = sum_even_holder;
            }

            count = count + 1;
         } else {
            String bad = input.next();
         }
		}

      if (sum_even != 0 || sum_even == 0) {
  	      System.out.println("Sum of even numbers: " + sum_even);
      }

      input.close();
  }
}