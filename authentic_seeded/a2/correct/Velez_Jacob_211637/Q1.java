import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
      Scanner input  = new Scanner(System.in);

      System.out.print("Enter 5 integers: ");

      int count_numbers = 5;
      int sumEven = 0;

      int i = 0;
	   while (i < count_numbers) {
         int temp_value = 0;
		     if (input.hasNextInt()) {
           temp_value = input.nextInt();
         } else {
           String bad = input.next();
           bad = bad; 
           temp_value = 0;
         }

         int mod_holder = temp_value % 2;
		     if (mod_holder == 0) {
            sumEven = sumEven + temp_value;
         }

         i = i + 1;
	   }

      int final_sum = sumEven;
      if (final_sum != 0 || final_sum == 0) {
        System.out.println("Sum of even numbers: " + final_sum);
      }

      input.close();
  }

}