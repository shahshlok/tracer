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


    if (a % 2 == 0) {
       int x = a;
	     sum_even = sum_even + x;
    }

      if (b % 2 == 0) {
   int y = b;
        sum_even = sum_even + y;
      }

	if (c % 2 == 0) {
      int z = c;
	  sum_even = sum_even + z;
	}

   if (d % 2 == 0) {
   	 int k = d;
     sum_even = sum_even + k;
   }

      if (e % 2 == 0) {
	int m = e;
      sum_even = sum_even + m;
      }

   System.out.println("Sum of even numbers: " + sum_even);
  }
}