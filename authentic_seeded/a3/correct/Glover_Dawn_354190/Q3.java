import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);
		System.out.print("Enter text: ");
  String line = input.nextLine();

      String upper_line = line.toUpperCase();

   String replaced_line = upper_line.replace(' ', '_');

     String result = replaced_line;

		System.out.println("Result: " + result);
  }
}