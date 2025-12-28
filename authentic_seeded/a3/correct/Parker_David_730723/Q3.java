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
      	String upper_line = line.toUpperCase();
      	String result = upper_line.replace(' ', '_');



      	if (result != null) {
      		System.out.println("Result: " + result);
      	}
      }
   }
}