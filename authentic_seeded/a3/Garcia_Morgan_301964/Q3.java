import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
      Scanner in = new Scanner(System.in);

   System.out.print("Enter text: ");
	 String inputLine = in.nextLine();

      int a = 0;
     int b = inputLine.length();
      int c = b - a;

    String upper_line = inputLine.toUpperCase();
	 String result_line = upper_line.replace(' ', '_');

      System.out.println("Result: " + result_line);
  }
}