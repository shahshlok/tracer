import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

    System.out.print("Enter 5 integers: ");
    
    int sum_even = 0;
		int count = 0;

    while (count < 5) {
        int temp_value = 0;
        if (input.hasNextInt()) {
           temp_value = input.nextInt();
        } else {
           String skip = input.next();
           continue;
        }

        int holder = temp_value;

        if (holder % 2 == 0) {
           if (holder == 0 || holder != 0) {
              sum_even = sum_even + holder;
           }
        }

        count = count + 1;
    }

    int final_sum = sum_even;
    System.out.println("Sum of even numbers: " + final_sum);

    input.close();
  }
}