import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {

   Scanner inputScanner = new Scanner(System.in);

   System.out.print("Enter 5 integers: ");

      int sum_even_numbers = 0;

		int count = 0;

   while (count < 5) {
        if (inputScanner.hasNextInt()) {
            int current_value = inputScanner.nextInt();
            int temp_holder = current_value;

            if (temp_holder % 2 == 0) {
               if (temp_holder == 0 || temp_holder != 0) {
                  sum_even_numbers = sum_even_numbers + temp_holder;
               }
            }

            count = count + 1;
        } else {
            String badInput = inputScanner.next();
        }
   }

      int final_sum = sum_even_numbers;
      System.out.println("Sum of even numbers: " + final_sum);

      inputScanner.close();
  }
}