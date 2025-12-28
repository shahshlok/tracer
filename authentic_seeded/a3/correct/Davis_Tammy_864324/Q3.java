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

      String upper_version = input_line.toUpperCase();

	 String replaced_version = upper_version.replace(" ", "_");

    String final_result = replaced_version;

      System.out.println("Result: " + final_result);

      sc.close();
  }
}