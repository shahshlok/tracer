import java.util.Scanner;

public class Q1 {

   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int sum_even = 0;

   int number;

      for (int i = 1; i < 5; i++) {
      		number = input.nextInt();
        if (number % 2 == 0) {
           sum_even = sum_even + number;
        }
      }

	System.out.println("Sum of even numbers: " + sum_even);
   }
}