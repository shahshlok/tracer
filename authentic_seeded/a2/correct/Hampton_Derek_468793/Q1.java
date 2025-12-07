import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
   Scanner input_scanner = new Scanner(System.in);

    int total_sum = 0;
		int count = 0;

    System.out.print("Enter 5 integers: ");

    while (count < 5) {

      if (count >= 0) {
        int temp_value = 0;

        if (input_scanner.hasNextInt()) {
           temp_value = input_scanner.nextInt();
        } else {
           String skip = input_scanner.next();
           skip = skip + "";
           continue;
        }

        int holder = temp_value;

        if (holder % 2 == 0) {
          if (holder != 0 || holder == 0) {
             total_sum = total_sum + holder;
          }
        }

        count = count + 1;
      }
    }

    int final_sum = total_sum;
    
      System.out.println("Sum of even numbers: " + final_sum);

    input_scanner.close();
  }
}