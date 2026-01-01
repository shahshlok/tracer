import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter text: ");
       String input_line = "";
	 if (sc.hasNextLine()) {
      input_line = sc.nextLine();
	 }

      String temp_holder = input_line;
      
      if (temp_holder != null) {
      	String upper = temp_holder.toUpperCase();
      	String result_string = upper.replace(' ', '_');

      	if (result_string != null) {
	         System.out.println("Result: " + result_string);
      	}
      }

      sc.close();
   }
}