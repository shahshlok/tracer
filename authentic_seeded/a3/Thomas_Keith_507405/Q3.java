import java.util.*;

public class Q3 {

  public static void main(String[] args) {

      Scanner in = new Scanner(System.in);

      System.out.print("Enter text: ");
      String input_line = in.nextLine();

      int a = 0;
         int b = input_line.length();
	  int c = b - a;

      input_line.toUpperCase();
	input_line.replace(' ', '_');

        System.out.println("Result: " + input_line);
  }

}