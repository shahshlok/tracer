import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int sum_even = 0;

      int a = sc.nextInt();
        int b = a % 2;
      if (b == 0) {
         sum_even = sum_even + a;
      }

	  int c = sc.nextInt();
    int d = c % 2;
	  if (d == 0) {
	       sum_even = sum_even + c;
	  }

   int e = sc.nextInt();
      int f = e % 2;
   if (f == 0) {
      sum_even = sum_even + e;
   }

		int g = sc.nextInt();
	 int h = g % 2;
	  if (h == 0) {
	      sum_even = sum_even + g;
	  }

    int i = sc.nextInt();
      int j = i % 2;
    if (j == 0) {
        sum_even = sum_even + i;
    }


      System.out.println("Sum of even numbers: " + sum_even);
   }
}