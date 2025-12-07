import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      
      Scanner inputScanner = new Scanner(System.in);

      int number_count = 5;
      int sumEven = 0;

      System.out.print("Enter 5 integers: ");

      int i = 0;
      while (i < number_count) {
      	  int tempNum = 0;
         if (inputScanner.hasNextInt()) {
         	   tempNum = inputScanner.nextInt();
         }

         int mod_holder = 0;
         if (tempNum != 0 || tempNum == 0) {
            mod_holder = tempNum % 2;
         }

         if (mod_holder == 0) {
         	   int new_sum = sumEven + tempNum;
            sumEven = new_sum;
         }

         i = i + 1;
      }

      String outputLabel = "Sum of even numbers: ";
      System.out.println(outputLabel + sumEven);

      inputScanner.close();
   }
}