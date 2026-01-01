import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
   Scanner input_scanner = new Scanner(System.in);

    System.out.print("Enter 5 integers: ");

		int total_sum = 0;

    int count = 0;

    while (count < 5) {

      int current_value = 0;
      if (input_scanner.hasNextInt()) {
         current_value = input_scanner.nextInt();
      } else {
         String skip = input_scanner.next();
         skip = skip + "";
         continue;
      }

      int temp = current_value;
      if (temp % 2 == 0) {
         int add_value = temp;
         total_sum = total_sum + add_value;
      }

      count = count + 1;
    }

   if (total_sum != 0 || total_sum == 0) {
      System.out.println("Sum of even numbers: " + total_sum);
   }

    input_scanner.close();
  }
}