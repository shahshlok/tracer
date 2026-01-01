import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter text: ");
        String input = scanner.nextLine();

        String upperInput = "";
        if (input != null) {
            upperInput = input.toUpperCase();
        }

        String result = upperInput.replace(" ", "_");

        System.out.println("Result: " + result);

        scanner.close();
    }
}