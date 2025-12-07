import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {

  	Scanner sc = new Scanner(System.in);

    System.out.print("Enter 5 integers: ");

    int sum_even = 0;

    for (int i = 0; i < 5; i++) {

      int x = sc.nextInt();

      int a = x / 2;
       int b = a * 2;
      	 int c = x - b;

      if (c == 0) {
      	  sum_even = sum_even + x;
      }
    }

      System.out.println("Sum of even numbers: " + sum_even);
  }
}