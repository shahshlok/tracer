import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

	 System.out.print("Enter text: ");
	 String input_line = in.nextLine();

   double a = 1;
      double b = 2;
   double c = a + b;


      input_line.toUpperCase();
  input_line.replace(" ", "_");


      System.out.println("Result: " + input_line);
  }
}