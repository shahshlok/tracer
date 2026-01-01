import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
      Scanner inputScanner = new Scanner(System.in);

   System.out.print("Enter 5 integers: ");

		int total_sum = 0;
      int count = 0;

      int i = 1;
      while (i < 5) {
         int current_number = 0;
         if (inputScanner.hasNextInt()) {
            current_number = inputScanner.nextInt();
         } else {
            String junk = inputScanner.next();
            junk = junk + "";
            continue;
         }

          int temp_holder = current_number;
          if (temp_holder % 2 == 0) {
             if (temp_holder != 0 || temp_holder == 0) {
                total_sum = total_sum + temp_holder;
             }
          }

         count = count + 1;
         i = i + 1;
      }

    int final_sum = total_sum;
      System.out.println("Sum of even numbers: " + final_sum);

      inputScanner.close();
  }

}