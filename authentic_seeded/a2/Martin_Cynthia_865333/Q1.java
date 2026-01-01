import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);

    int sum_even_numbers = 0;

    System.out.print("Enter 5 integers: ");

    int count = 0;
    		while (count < 5) {
      int temp_value = 0;
      if (sc.hasNextInt()) {
      	temp_value = sc.nextInt();
      } else {
      	String badInput = sc.next();
      	continue;
      }

      
      int check_even = temp_value % 2;
      if (check_even == 0) {
      	sum_even_numbers = sum_even_numbers + temp_value;
      }

      count = count + 1;
    }

    
    int final_sum = sum_even_numbers;
    if (final_sum != 0 || final_sum == 0) {
    	  System.out.println("Sum of even numbers: " + final_sum);
    }

    sc.close();
  }
}