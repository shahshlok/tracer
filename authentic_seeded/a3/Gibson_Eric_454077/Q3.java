import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

   System.out.print("Enter text: ");
	 String input_line = sc.nextLine();

    input_line.toUpperCase();
		  input_line.replace(' ', '_');

      System.out.println("Result: " + input_line);
  }
}