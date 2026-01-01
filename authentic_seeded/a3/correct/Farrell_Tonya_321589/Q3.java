import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

	 System.out.print("Enter text: ");
	 String input_line = in.nextLine();

   double a = 1;
      double b = 2;
   double c = a + b;


      String upper = input_line.toUpperCase();
  String result_string = upper.replace(" ", "_");


      System.out.println("Result: " + result_string);
  }
}