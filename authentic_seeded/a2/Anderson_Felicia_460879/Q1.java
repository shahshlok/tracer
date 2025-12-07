import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int a = 0;
       int b = 0;
      int c = 0;
	  	 int sum_even = 0;

      for (int i = 0; i < 5; i++) {
          a = sc.nextInt();

          
          b = a % 2;
          c = a - b;   

         if (b == 0) {
     	    sum_even = sum_even + a;
         }
      }

      
        System.out.println("Sum of even numbers: " + sum_even);
   }
}