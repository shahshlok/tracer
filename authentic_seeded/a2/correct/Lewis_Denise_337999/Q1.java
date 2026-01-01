import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter 5 integers: ");

		int a = sc.nextInt();
    int b = sc.nextInt();
      int c = sc.nextInt();
		int d = sc.nextInt();
   int e = sc.nextInt();

    
    int sum_even = 0;

		int x = a % 2;
  int y = b % 2;
    int z = c % 2;
		int u = d % 2;
  int v = e % 2;

    if (x == 0) {
		  sum_even = sum_even + a;
    }

   if (y == 0) {
      sum_even = sum_even + b;
	 }

    if (z == 0) {
     sum_even = sum_even + c;
    }

	 if (u == 0) {
      sum_even = sum_even + d;
  }

   if (v == 0) {
		 sum_even = sum_even + e;
    }

    System.out.println("Sum of even numbers: " + sum_even);
  }
}