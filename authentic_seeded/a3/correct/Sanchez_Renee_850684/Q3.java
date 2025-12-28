import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter text: ");
   String line = input.nextLine();

      String upper_line = line.toUpperCase();
		
 String result = upper_line.replace(' ', '_');

      System.out.println("Result: " + result);
  }
}