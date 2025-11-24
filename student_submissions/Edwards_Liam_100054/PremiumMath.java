import java.util.Scanner;

public class PremiumMath {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = sc.nextInt();
        int accidents = sc.nextInt();

        if (age < 0 || accidents < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || accidents > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        int premium = 600;
        if (age < 24) {
            premium += 200;
        }
        if (accidents > 0) {
            // Logic Error: Integer division 1/4 is 0, so no surcharge is added
            premium += premium * (1 / 4);
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
