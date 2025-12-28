import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter text: ");
    String original_line = "";

    if (input.hasNextLine()) {
        String temp_holder = input.nextLine();
        original_line = temp_holder;
    }

    
    if (original_line == null) {
       original_line = "";
    }

    	 String upper_line = original_line.toUpperCase();
    String result_line = upper_line.replace(' ', '_');

    
    if (result_line != null) {
      System.out.println("Result: " + result_line);
    } else {
      String empty_result = "";
      System.out.println("Result: " + empty_result);
    }

    input.close();
  }
}