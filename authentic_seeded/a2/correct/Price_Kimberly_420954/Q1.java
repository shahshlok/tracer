import java.util.Scanner;

public class Q1 {

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int a = 0;
       int b = 0;
	  int c = 0;

      int sum_even = 0;

      int n1 = sc.nextInt();
      a = n1 % 2;
      if (a == 0) {
         sum_even = sum_even + n1;
      }

      int n2 = sc.nextInt();
       b = n2 % 2;
	  if (b == 0) {
	    sum_even = sum_even + n2;
      }

	  int n3 = sc.nextInt();
      c = n3 % 2;
      if (c == 0) {
          sum_even = sum_even + n3;
      }

      int n4 = sc.nextInt();
      a = n4 % 2;
        if (a == 0) {
	      sum_even = sum_even + n4;
	    }

	 int n5 = sc.nextInt();
      b = n5 % 2;
      if (b == 0) {
      	sum_even = sum_even + n5;
      }

      System.out.println("Sum of even numbers: " + sum_even);

      sc.close();
   }
}