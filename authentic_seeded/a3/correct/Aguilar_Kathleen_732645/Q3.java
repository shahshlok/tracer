import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
      Scanner inputReader = new Scanner(System.in);

   System.out.print("Enter text: ");
   String original_text = inputReader.nextLine();

   int a = 2;
      int b = 3;
		int c = a + b;



      String upper_text = original_text.toUpperCase();
		
		String shouted_text = upper_text.replace(' ', '_');

   System.out.println("Result: " + shouted_text);
      
      inputReader.close();
  }
}