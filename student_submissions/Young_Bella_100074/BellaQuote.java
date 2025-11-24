import java.util.Scanner;

public class BellaQuote {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        String ageStr = sc.next();
        String accStr = sc.next();

        int age = Integer.parseInt(ageStr);
        int acc = Integer.parseInt(accStr);

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double premium = 600;
        if (age < 24) {
            premium += 200;
        }
        if (acc > 0) {
            premium += premium * 0.25;
        }

        System.out.println("Your insurance is " + premium);
        System.out.print("Enter your email: ");
        String email = sc.next();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
