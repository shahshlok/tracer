import java.util.*;

public class Q3 {

   public static void main(String[] args) {
	Scanner input = new Scanner(System.in);

      System.out.print("Enter text: ");
      String line = "";
      if (input.hasNextLine()) {
         String temp = input.nextLine();
         line = temp;
      }

      if (line != null) {
      	String upper_line = line;
      	upper_line.toUpperCase();
      	upper_line.replace(' ', '_');



      	if (upper_line != null) {
      		System.out.println("Result: " + line);
      	}
      }
   }
}