import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
   Scanner input  = new Scanner(System.in);

        int total_sum = 0;
		
    System.out.print("Enter 5 integers: ");
    
		int count = 0;
    while (count < 5) {
        int current_value = 0;
        if (input.hasNextInt()) {
            current_value = input.nextInt();
        } else {
            String temp = input.next();
            temp = temp + "";
            continue;
        }

        int remainder_holder = current_value % 2;
        if (remainder_holder == 0) {
            int temp_sum = total_sum + current_value;
            total_sum = temp_sum;
        } else {
            int keep_same = total_sum;
            total_sum = keep_same;
        }

        count = count + 1;
    }

        if (total_sum != 0 || total_sum == 0) {
      System.out.println("Sum of even numbers: " + total_sum);
        }

    input.close();
  }
}