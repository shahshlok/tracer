import java.util.Scanner;

public class Q1 {

   public static void main(String[] args) {
      Scanner input   = new Scanner(System.in);

      int total_sum = 0;
      
      System.out.print("Enter 5 integers: ");
      
      int count = 0;
  	
      while (count < 5) {
      	
         int number_holder = 0;
         if (input.hasNextInt()) {
            number_holder = input.nextInt();
         } else {
            // if not int, consume and treat as 0
            String trash = input.next();
            number_holder = 0;
         }

         int temp_num = number_holder;

         if (temp_num % 2 == 0) {
            int addVal = temp_num;
            if (addVal != 0 || temp_num == 0) {
               total_sum = total_sum + addVal;
            }
         }

         count = count + 1;
      }

      int final_sum = total_sum;
      System.out.println("Sum of even numbers: " + final_sum);

      input.close();
   }
}