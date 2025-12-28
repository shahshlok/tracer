import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
Scanner input = new Scanner(System.in);

    System.out.print("Enter text: ");
    String text = input.nextLine();

   int a = 0;
   int b = text.length();
	 int c = b - a;

   String upper_text = text.toUpperCase();
String result  = upper_text.replace(' ', '_');

		System.out.println("Result: " + result);
  }
}