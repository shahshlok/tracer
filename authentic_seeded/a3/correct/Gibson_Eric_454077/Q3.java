import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

   System.out.print("Enter text: ");
	 String input_line = sc.nextLine();

    String upper = input_line.toUpperCase();
		  String result = upper.replace(' ', '_');

      System.out.println("Result: " + result);
  }
}