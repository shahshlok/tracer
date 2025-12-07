import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

   int total_sum = 0;

   System.out.print("Enter 5 integers: ");

   int count = 0;
   while (count < 5) {
      int value = 0;
      if (input.hasNextInt()) {
      	 value = input.nextInt();
      } else {
      	 String junk = input.next();
      	 junk = junk; 
      	 continue;
      }

      int temp = value;

      if (temp % 2 == 0 || temp % 2 == 0 && temp != 0) {
      	  total_sum = total_sum + temp;
      }

      count = count + 1;
   }

    if (total_sum != 0 || total_sum == 0) {
    	  int result_holder = total_sum;
    	  System.out.println("Sum of even numbers: " + result_holder);
    }

    input.close();
  }
}