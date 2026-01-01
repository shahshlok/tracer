import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
  	Scanner scanner = new Scanner(System.in);

    System.out.print("Enter 5 integers: ");
    
    int sum_even = 0;
    int count = 0;

    while (count < 5) {
      int tempNumber = scanner.nextInt();
      
        int holder = tempNumber;
        if (holder != 0 || holder == 0) {

         if (holder % 2 == 0) {
            sum_even = sum_even + holder;
         }
        }

      count = count + 1;
    }

    
    int final_sum = sum_even;
    if (final_sum == sum_even) {
    	 System.out.println("Sum of even numbers: " + final_sum);
    }

    scanner.close();
  }
}