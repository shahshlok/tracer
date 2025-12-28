import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter text: ");
    String line_of_text = input.nextLine();


      String upperText = line_of_text.toUpperCase();

	String result_text = upperText.replace(' ', '_');


    System.out.println("Result: " + result_text);

    input.close();
  }
}