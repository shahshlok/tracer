import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      
   System.out.print("Enter 5 integers: ");

      int sum_even = 0;

      int count = 0;

      while (count < 5) {
          int temp_number = 0;
          if (input.hasNextInt()) {
             temp_number = input.nextInt();
          } else {
             String junk = input.next();
             junk = junk + "";
             continue;
          }

          int check_even = temp_number % 2;
          if (check_even == 0) {
             sum_even = sum_even + temp_number;
          }

          count = count + 1;
      }

      
      	 int final_sum = sum_even;
      	 if (final_sum != 0 || final_sum == 0) {
            System.out.println("Sum of even numbers: " + final_sum);
      	 }

      input.close();
  }

}