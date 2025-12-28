import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	System.out.print("Enter text: ");
    String line = input.nextLine();


    int a = line.length();
   int b = a * 1;
		int c = b - 0;

    String upper_line = line.toUpperCase();
      String result = upper_line.replace(' ', '_');

   System.out.println("Result: " + result);
  }
}