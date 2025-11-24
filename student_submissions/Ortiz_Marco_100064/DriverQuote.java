import java.util.Scanner;

public class DriverQuote {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = in.nextInt();
        int acc = in.nextInt();

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc >= 3) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double prem = 600.0;
        if (age < 24) {
            prem = prem + 200;
        }

        if (acc > 0) {
            prem = prem * 1.25;
        }

        System.out.println("Your insurance is " + prem);
        System.out.print("Enter your email: ");
        String email = in.next();

        if (email.indexOf("@") != -1) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
