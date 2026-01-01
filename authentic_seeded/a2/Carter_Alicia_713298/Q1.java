import java.util.*;

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

      int i = 1;
      int x;

      while (i < 5) {
      	x = sc.nextInt();

      	if (i == 1) {
      		a = x;
      	} else if (i == 2) {
      	  b = x;
      	} else if (i == 3) {
      	   c = x;
      	} else if (i == 4) {
      	    d = x;
      	} else {
      	 	 e = x;
      	}

      	if (x % 2 == 0) {
      		sum_even = sum_even + x;
      	}

      	i = i + 1;
      }

      System.out.println("Sum of even numbers: " + sum_even);
   }
}