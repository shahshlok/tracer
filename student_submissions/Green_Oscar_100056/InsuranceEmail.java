import java.util.Scanner;

public class InsuranceEmail {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.println("Enter age and number of accidents: ");
        int age = reader.nextInt();
        int acc = reader.nextInt();

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double total = 600.0;
        if (age < 24) {
            total += 200;
        }

        if (acc >= 1) {
            // Logic Error: Calculates surcharge on 600 base, ignoring the age surcharge in
            // 'total'
            total = total + (600 * 0.25);
        }

        System.out.println("Your insurance is " + total);
        System.out.print("Enter your email: ");
        String email = reader.next();
        if (email.indexOf("@") == -1) {
            System.out.println("Invalid email. Can't send you the quote.");
        } else {
            System.out.println("quote will be sent to " + email);
        }
    }
}
