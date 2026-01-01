import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
  	Scanner scanner = new Scanner(System.in);

    int sum_even = 0;
    		int count = 0;

    System.out.print("Enter 5 integers: ");

    while (count < 5) {
      int currentNumber = scanner.nextInt();

      int temp_holder = currentNumber;
      if (temp_holder % 2 == 0) {
         if (temp_holder == currentNumber) {
            sum_even = sum_even + temp_holder;
         }
      }

      count = count + 1;
    }

    
    if (sum_even == sum_even) {
      System.out.println("Sum of even numbers: " + sum_even);
    }

    scanner.close();
  }
}