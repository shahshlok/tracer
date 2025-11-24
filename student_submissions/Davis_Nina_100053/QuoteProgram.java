import java.util.Scanner;

public class QuoteProgram {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter age and number of accidents: ");
        int age = s.nextInt();
        int crashes = s.nextInt();

        if (age < 0 || crashes < 0) {
            System.out.println("Invalid age or number of accidents.");
        } Else if (age < 18 || crashes > 2) { // Capitalized Else
            System.out.println("You are not eligible for insurance.");
        } else {
            double price = 600;
            if (age < 24) {
                price += 200;
            }
            if (crashes >= 1) {
                price = price + price * 0.25;
            }
            System.out.println("Your insurance is " + price);
            System.out.print("Enter your email: ");
            String email = s.next();
            if (email.indexOf("@") != -1) {
                System.out.println("quote will be sent to " + email);
            } else {
                System.out.println("Invalid email. Can't send you the quote.");
            }
        }
    }
}
