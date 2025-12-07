import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner input   = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int sum_even = 0;
      
		int count = 0;

      while (count < 5) {
         int number_holder = 0;
         if (input.hasNextInt()) {
            number_holder = input.nextInt();
         } else {
            String skip = input.next();
            continue;
         }

         int temp = number_holder;

         if (temp % 2 == 0) {
            if (temp != 0 || temp == 0) {
               sum_even = sum_even + temp;
            }
         }

         count = count + 1;
      }

      int result_sum = sum_even;
      
      System.out.println("Sum of even numbers: " + result_sum);

      input.close();
   }
}