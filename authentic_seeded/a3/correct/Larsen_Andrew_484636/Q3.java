import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter text: ");
   String user_text = input.nextLine();

      String upper = user_text.toUpperCase();

	String result = upper.replace(' ', '_');

      System.out.println("Result: " + result);
  }
}