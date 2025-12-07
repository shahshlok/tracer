import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);
    
    int sum_even = 0;
    
    System.out.print("Enter 5 integers: ");
    
    int num1 = sc.nextInt();
      if(num1 % 2 == 0) {
       sum_even = sum_even + num1;
      }

      int num2 = sc.nextInt();
        if(num2 % 2 == 0) {
  	    sum_even = sum_even + num2;
        }

    int num3 = sc.nextInt();
      if(num3 % 2 == 0) {
        sum_even = sum_even + num3;
      }

        int num4 = sc.nextInt();
    if(num4 % 2 == 0) {
    	 sum_even = sum_even + num4;
    }

      int num5 = sc.nextInt();
      if(num5 % 2 == 0) {
      	  sum_even = sum_even + num5;
      }

    
      System.out.println("Sum of even numbers: " + sum_even);
  }
}