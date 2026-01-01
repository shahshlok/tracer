import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    int a, b, c, d, e;
	 int sum_even = 0;

    System.out.print("Enter 5 integers: ");

   a = sc.nextInt();
   b = sc.nextInt();
	 c = sc.nextInt();
    d = sc.nextInt();
    e = sc.nextInt();



    if (a % 2 == 0) {
      sum_even = sum_even + a;
    }

	  if (b % 2 == 0) {
      sum_even = sum_even + b;
	  }

   if (c % 2 == 0) {
      sum_even = sum_even + c;
   }

    if (d % 2 == 0) {
	      sum_even = sum_even + d;
    }

	 if (e % 2 == 0) {
		  sum_even = sum_even + e;
	 }

   System.out.println("Sum of even numbers: " + sum_even);
  }
}