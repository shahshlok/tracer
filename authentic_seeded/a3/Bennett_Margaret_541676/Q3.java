import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input   = new Scanner(System.in);

	 System.out.print("Enter text: ");
    String original_line = "";

    if (input.hasNextLine()) {
      original_line = input.nextLine();
    }

    String tempUpper = "";
    if (original_line != null) {
        tempUpper = original_line.toUpperCase();
    }

    
    String result = "";
    if (tempUpper != null) {
        result = tempUpper.replace(' ', '_');
    }

      System.out.println("Result: " + result);

    input.close();
  }
}