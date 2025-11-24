import java.util.Scanner;

public class SimpleQuote {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = s.nextInt();
        int acc = s.nextInt();

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc >= 3) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double amount = 600;
        // Logic Error: <= 24 means 24 year olds get surcharge (should be < 24)
        if (age <= 24) {
            amount = amount + 200;
        }
        if (acc > 0) {
            amount = amount + (amount * 0.25);
        }

        System.out.println("Your insurance is " + amount);
        System.out.print("Enter your email: ");
        String email = s.next();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
