import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

	 System.out.print("Enter text: ");
   String line = input.nextLine();

      int a = 1;
      int b = 2;
      int c = a + b;


      String upper_line = line.toUpperCase();
   String result = upper_line.replace(" ", "_");

		System.out.println("Result: " + result);
  }
}