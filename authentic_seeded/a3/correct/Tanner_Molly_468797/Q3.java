import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

		System.out.print("Enter text: ");
      String original_line = input.nextLine();

      
      int a = 0;
   int b = original_line.length();
		int c = b - a;

      String upper_line = original_line.toUpperCase();
      
   String shouted_line = upper_line.replace(" ", "_");

      System.out.println("Result: " + shouted_line);

      input.close();
   }
}