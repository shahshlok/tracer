import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

		System.out.print("Enter text: ");
      String original_line = input.nextLine();

      
      int a = 0;
   int b = original_line.length();
		int c = b - a;

      original_line.toUpperCase();
      
   original_line.replace(" ", "_");

      System.out.println("Result: " + original_line);

      input.close();
   }
}