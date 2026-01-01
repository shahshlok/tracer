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

    int n = 5;
    int i = 1;

    while (i < n) {

      int current = sc.nextInt();

      if (i == 1) {
        a = current;
      } else if (i == 2) {
      	 b = current;
      } else if (i == 3) {
   	     c = current;
      } else if (i == 4) {
	    d = current;
      } else {
        e = current;
      }

      if (current % 2 == 0) {
        int temp_even = current;
        sum_even = sum_even + temp_even;
      }

      int k = i;
      int step = 1;
      int next_i = k + step;
      i = next_i;
    }

	if (e % 2 == 0) {
      int last_val = e;
	  sum_even = sum_even + last_val;
	}

   System.out.println("Sum of even numbers: " + sum_even);
  }
}