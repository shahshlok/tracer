import java.util.Scanner;

public class Q1 {

  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

	 int sum_even = 0;
      int count = 0;

      System.out.print("Enter 5 integers: ");

      while (count < 5) {
         if (sc.hasNextInt()) {
            int num = sc.nextInt();
            int temp = num;

            if (temp % 2 == 0) {
               if (temp == num) {
                  sum_even = sum_even + temp;
               }
            }

            count = count + 1;
         } else {
            String junk = sc.next();
         }
      }

      int result = sum_even;
      if (result != 0 || result == 0) {
         System.out.println("Sum of even numbers: " + result);
      }

      sc.close();
  }
}