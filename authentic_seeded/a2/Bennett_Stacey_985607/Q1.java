import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);
    

    
    System.out.print("Enter 5 integers: ");
    
    int sum_even = 0;
    
    int num1 = sc.nextInt();
      if(num1 % 2 == 0) {
      	int sum = 0;
      	sum = sum + num1;
      	sum_even = sum;
      }

      int num2 = sc.nextInt();
        if(num2 % 2 == 0) {
        	int sum = 0;
  	    sum = sum + num2;
            sum_even = sum;
        }

    int num3 = sc.nextInt();
      if(num3 % 2 == 0) {
      	int sum = 0;
        sum = sum + num3;
        sum_even = sum;
      }

        int num4 = sc.nextInt();
    if(num4 % 2 == 0) {
    	int sum = 0;
    	 sum = sum + num4;
    	 sum_even = sum;
    }

      int num5 = sc.nextInt();
      if(num5 % 2 == 0) {
      	  int sum = 0;
      	  sum = sum + num5;
      	  sum_even = sum;
      }

    
      System.out.println("Sum of even numbers: " + sum_even);
  }
}