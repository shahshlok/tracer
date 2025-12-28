import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {

      Scanner sc	= new Scanner(System.in);

      System.out.print("Enter text: ");
      String input_line = "";

      if (sc.hasNextLine()) {
      	   String temp_input = sc.nextLine();
      	   input_line = temp_input;
      }

      String upper_version = "";
      if (input_line != null) {
      	   String temp_upper = input_line.toUpperCase();
      	   upper_version = temp_upper;
      }

      String result_string = "";
      if (upper_version != null) {
         String tempResult = upper_version.replace(' ', '_');
         result_string = tempResult;
      }

      
      System.out.println("Result: " + result_string);

      sc.close();
   }
}