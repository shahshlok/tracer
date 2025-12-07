import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

   int sum_even = 0;
		int count = 0;

   System.out.print("Enter 5 integers: ");
   while (count < 5) {
      int num = input.nextInt();

      if (num % 2 == 0) {
       sum_even = sum_even + num;
      }
      count = count + 1;
   }

      System.out.println("Sum of even numbers: " + sum_even);
  }
}