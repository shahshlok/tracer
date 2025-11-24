import java.util.Scanner;

public class InsuranceCheck {
    public static void main(String[] args) {
        // Create scanner
        Scanner kb = new Scanner(System.in);
        // Ask user for input
        System.out.print("Enter age and number of accidents: ");
        int age = kb.nextInt();
        int acc = kb.nextInt();

        // Check for negatives
        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        // Check eligibility
        // TODO: check if age limit is 18 or 21?
        if (age < 18 || acc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double total = 600;
        // Age surcharge
        if (age < 24) {
            total += 200;
        }
        // Accident surcharge
        if (acc > 0) {
            total = total + (total * 0.25);
        }

        // Print result
        System.out.println("Your insurance is " + total);
        System.out.print("Enter your email: ");
        String email = kb.next();
        // Validate email
        // FIXME: use regex later
        if (email.contains("@")) { // Changed from == "@" which was a logic error in previous read, but keeping it
                                   // simple style disaster here
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
