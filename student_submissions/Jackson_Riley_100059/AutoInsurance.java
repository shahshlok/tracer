import java.util.Scanner;

public class AutoInsurance {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = input.nextInt();
        int acc = input.nextInt();

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double bill = 600;
        if (age < 24) {
            bill += 200;
        }
        if (acc > 0) {
            // Logic Error: Adds 25 dollars instead of 25%
            bill += 25;
        }

        System.out.println("Your insurance is " + bill);
        System.out.print("Enter your email: ");
        String email = input.next();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
