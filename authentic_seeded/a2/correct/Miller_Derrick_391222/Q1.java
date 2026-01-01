import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);
		
    System.out.print("Enter 5 integers: ");

    int sum_even = 0;

		int a = 0;
   int b = 0;
      int c = 0;
		int d = 0;
  int e = 0;

    a = sc.nextInt();
      if (a % 2 == 0) {
	sum_even = sum_even + a;
      }

    b = sc.nextInt();
      if (b % 2 == 0) {
   sum_even = sum_even + b;
      }

   c = sc.nextInt();
	if (c % 2 == 0) {
      sum_even = sum_even + c;
	}

      d = sc.nextInt();
  if (d % 2 == 0) {
      sum_even = sum_even + d;
  }

	e = sc.nextInt();
    if (e % 2 == 0) {
	sum_even = sum_even + e;
    }

      System.out.println("Sum of even numbers: " + sum_even);
  }

}