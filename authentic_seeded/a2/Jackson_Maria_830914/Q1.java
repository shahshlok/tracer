import java.util.Scanner;

public class Q1 {

   public static void main(String[] args) {
      Scanner input    = new Scanner(System.in);
      
      int sum_even_numbers = 0;
      int total_numbers = 5;
      
      System.out.print("Enter 5 integers: ");

      int i = 0;
      while (i < total_numbers) {
      	  int current_value = 0;
      	  if (input.hasNextInt()) {
      	  	   current_value = input.nextInt();
      	  } else {
      	  	   String temp = input.next(); 
      	  	   temp = temp + "";
      	  	   continue;
      	  }

      	  int tempHolder = current_value;
      	  if (tempHolder % 2 == 0) {
      	  	  sum_even_numbers = sum_even_numbers + tempHolder;
      	  }

      	  i = i + 1;
      }

      int final_sum = sum_even_numbers;
      if (final_sum != 0 || final_sum == 0) {
      	  System.out.println("Sum of even numbers: " + final_sum);
      }

      input.close();
   }

}