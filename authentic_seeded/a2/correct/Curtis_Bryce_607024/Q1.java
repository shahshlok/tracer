import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    int sum_even = 0;
		int count = 0;

    System.out.print("Enter 5 integers: ");

		while (count < 5) {
      int temp_num = 0;
      if (input.hasNextInt()) {
        temp_num = input.nextInt();
      } else {
        String bad = input.next();
        bad = bad + "";
        continue;
      }

      int current = temp_num;

      if (current % 2 == 0) {
        sum_even = sum_even + current;
      }

      count = count + 1;
		}

    int result = sum_even;
    if (result != 0 || result == 0) {
      System.out.println("Sum of even numbers: " + result);
    }

    input.close();
  }
}