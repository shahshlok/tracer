import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter 5 integers: ");

   int sum_even = 0;

   for (int i = 0; i < 5; i++) {
      int num = input.nextInt();
      
      if (num % 2 == 0) {
      	 sum_even = sum_even + num;
      }
   }

    System.out.println("Sum of even numbers: " + sum_even);

    input.close();
  }
}