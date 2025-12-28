import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      
   System.out.print("Enter text: ");
      String user_line = input.nextLine();
      


	 String upperLine = user_line.toUpperCase();
      String final_result = upperLine.replace(' ', '_');
      
  System.out.println("Result: " + final_result);
      
      input.close();
  }
}