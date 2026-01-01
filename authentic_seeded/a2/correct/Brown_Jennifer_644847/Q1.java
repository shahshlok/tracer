import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {

      Scanner    sc = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int sum_even = 0;

      int a, b, c;

      for (int i = 0; i < 5; i++) {
         int num = sc.nextInt();

         a = num % 2;
            b = num;
     	    c = sum_even;

         if (a == 0) {
            sum_even = c + b;
         }
      }

        System.out.println("Sum of even numbers: " + sum_even);
   }
}