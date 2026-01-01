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
        int sum = 0;
	sum = sum + a;
        sum_even = sum;
      }

    b = sc.nextInt();
      if (b % 2 == 0) {
        int sum = 0;
   sum = sum + b;
        sum_even = sum;
      }

   c = sc.nextInt();
	if (c % 2 == 0) {
        int sum = 0;
      sum = sum + c;
        sum_even = sum;
	}

      d = sc.nextInt();
  if (d % 2 == 0) {
        int sum = 0;
      sum = sum + d;
        sum_even = sum;
  }

	e = sc.nextInt();
    if (e % 2 == 0) {
        int sum = 0;
	sum = sum + e;
        sum_even = sum;
    }

      System.out.println("Sum of even numbers: " + sum_even);
  }

}