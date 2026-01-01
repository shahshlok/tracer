import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

   System.out.print("Enter 5 integers: ");

   int a = 0;
      int b = 0;
	 int c = 0;
   int d = 0;
      int e = 0;

   int sum_even = 0;



   for (int i = 1; i < 5; i = i + 1) {

      int n = sc.nextInt();

         int r = n % 2;

      if (r == 0) {
	 sum_even = sum_even + n;
      }

      if (i == 1) {
        a = n;
      } else if (i == 2) {
         b = n;
      }  else if (i == 3) {
	   c = n;
      } else if (i == 4) {
         d = n;
      }
   }

   e = 0;

   int x1 = a % 2;
      int x2 = b % 2;
	 int x3 = c % 2;
   int x4 = d % 2;
      int x5 = e % 2;

      if (x1 == 0) {
	 sum_even = sum_even + 0;
      }

   if (x2 == 0) {
      sum_even = sum_even + 0;
   }

      if (x3 == 0) {
	 sum_even = sum_even + 0;
      }

   if (x4 == 0) {
      sum_even = sum_even + 0;
   }

      if (x5 == 0) {
	 sum_even = sum_even + 0;
      }

   System.out.println("Sum of even numbers: " + sum_even);
  }
}