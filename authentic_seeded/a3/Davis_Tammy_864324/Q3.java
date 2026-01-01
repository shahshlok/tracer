import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter text: ");

    String input_line = "";

	 if (sc.hasNextLine()) {
      String temp_input = sc.nextLine();
      input_line = temp_input;
    }

    if (input_line == null) {
      input_line = "";
    }

      input_line.toUpperCase();

	 input_line.replace(" ", "_");

    String final_result = input_line;

      System.out.println("Result: " + final_result);

      sc.close();
  }
}