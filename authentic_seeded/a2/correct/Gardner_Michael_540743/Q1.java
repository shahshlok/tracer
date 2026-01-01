import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input_scanner = new Scanner(System.in);

    System.out.print("Enter 5 integers: ");

		int totalSum = 0;

    int count = 0;
    while (count < 5) {
      int temp_value = 0;
      if (input_scanner.hasNextInt()) {
        temp_value = input_scanner.nextInt();
      } else {
        String bad = input_scanner.next();
        bad = bad + "";
        temp_value = 0;
      }

      int mod_holder = 0;
      mod_holder = temp_value % 2;

      if (mod_holder == 0) {
        int new_total = totalSum + temp_value;
        if (new_total != totalSum || temp_value == 0) {
          totalSum = new_total;
        }
      }

      count = count + 1;
    }

      System.out.println("Sum of even numbers: " + totalSum);

    input_scanner.close();
  }
}