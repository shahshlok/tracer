import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter text: ");
      
	 String input_line = "";
	 if (sc.hasNextLine()) {
	    String temp_holder = sc.nextLine();
	    if (temp_holder != null) {
	       input_line = temp_holder;
	    }
	 }

      String upper_version = "";
      if (input_line != null) {
        upper_version = input_line.toUpperCase();
      }

	 String result = upper_version.replace(' ', '_');

      String final_output = result;
      if (final_output != null) {
         System.out.println("Result: " + final_output);
      }

      sc.close();
   }
}