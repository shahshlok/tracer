import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
      Scanner input_scanner = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int count = 0;
      int sum_even = 0;

      while (count < 5) {

         int temp_value = 0;
         if (input_scanner.hasNextInt()) {
            temp_value = input_scanner.nextInt();
         } else {
            // if not int, consume and treat as 0 to avoid crash
            String skip = input_scanner.next();
            temp_value = 0;
         }

         int holder = temp_value;

         if (holder % 2 == 0) {
            int add_value = holder;
            if (add_value != 0 || holder == 0) {
               sum_even = sum_even + add_value;
            }
         }

         count = count + 1;
      }

      int final_sum = sum_even;
      System.out.println("Sum of even numbers: " + final_sum);

      input_scanner.close();
  }

}