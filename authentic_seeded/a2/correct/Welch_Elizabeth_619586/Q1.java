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

   a = sc.nextInt();
   b = sc.nextInt();
   c = sc.nextInt();
   d = sc.nextInt();
   e = sc.nextInt();

   int x1 = a % 2;
      int x2 = b % 2;
	 int x3 = c % 2;
   int x4 = d % 2;
      int x5 = e % 2;



      if (x1 == 0) {
	 sum_even = sum_even + a;
      }

   if (x2 == 0) {
      sum_even = sum_even + b;
   }

      if (x3 == 0) {
	 sum_even = sum_even + c;
      }

   if (x4 == 0) {
      sum_even = sum_even + d;
   }

      if (x5 == 0) {
	 sum_even = sum_even + e;
      }

   System.out.println("Sum of even numbers: " + sum_even);
  }
}