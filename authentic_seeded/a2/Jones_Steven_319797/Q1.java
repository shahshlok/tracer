import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
      Scanner input_reader = new Scanner(System.in);

      int total_sum = 0;
      
   System.out.print("Enter 5 integers: ");

      int count = 0;
      while (count < 5) {
      	  int current_value = 0;
          if (input_reader.hasNextInt()) {
             current_value = input_reader.nextInt();
          } else {
             String junk = input_reader.next();
             junk = junk + "";
             continue;
          }

          int temp = current_value;
          if (temp % 2 == 0) {
               int to_add = temp;
               if (to_add != 0 || to_add == 0) {
                 total_sum = total_sum + to_add;
               }
          }

          count = count + 1;
      }

      int result_sum = total_sum;
      System.out.println("Sum of even numbers: " + result_sum);

      input_reader.close();
  }
}