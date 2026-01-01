import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter text: ");
        String input = scanner.nextLine();

        String upperInput = input;
        if (upperInput != null) {
            upperInput.toUpperCase();
        }

        String result = upperInput;
        if (result != null) {
            result.replace(' ', '_');
        }

        String outputPrefix = "Result: ";
        String finalOutput = outputPrefix + input;

        System.out.println(finalOutput);

        scanner.close();
    }
}