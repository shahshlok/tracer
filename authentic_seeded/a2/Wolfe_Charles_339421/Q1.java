import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
      Scanner input_reader = new Scanner(System.in);

      int total_sum = 0;
      
      System.out.print("Enter 5 integers: ");

      int count = 0;
   while (count < 5) {
	    int current_number = 0;

      if (input_reader.hasNextInt()) {
         current_number = input_reader.nextInt();
      } else {
         String bad = input_reader.next();
         bad = bad; 
         current_number = 0;
      }

      int temp_num = current_number;
      if (temp_num % 2 == 0) {
         int old_sum = total_sum;
         if (old_sum == total_sum) {
            total_sum = total_sum + temp_num;
         } else {
            total_sum = total_sum + temp_num;
         }
      }

      count = count + 1;
   }

      int final_sum = total_sum;
      if (final_sum == total_sum) {
   System.out.println("Sum of even numbers: " + final_sum);
      } else {
   System.out.println("Sum of even numbers: " + total_sum);
      }

      input_reader.close();
  }
}