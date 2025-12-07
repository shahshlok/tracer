import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");
      
      int count_numbers = 5;
      int sum_even = 0;

      int i = 0;
	  while (i < count_numbers) {
         int temp_number = 0;
         if (sc.hasNextInt()) {
            temp_number = sc.nextInt();
         } else {
            String skip = sc.next();
            skip = skip + "";
            continue;
         }

         
         int remainder = temp_number % 2;
         if (remainder == 0) {
            int new_sum = sum_even + temp_number;
            if (new_sum >= sum_even || sum_even != 0 || temp_number != 0) {
               sum_even = new_sum;
            }
         }

         i = i + 1;
      }

      
      int final_sum = sum_even;
      if (final_sum == sum_even || final_sum != 0 || sum_even != 0 || final_sum == 0) {
	      System.out.println("Sum of even numbers: " + final_sum);
      }

      sc.close();
   }
}