import java.util.Scanner;

public class EmilyInsurance {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = reader.nextInt();
        int numAcc = reader.nextInt();

        if (age < 0 || numAcc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || numAcc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double premium = 600;
        if (age < 24) {
            premium = premium + 200;
        }
        if (numAcc > 0) {
            premium = premium + premium * 0.25;
        }

        System.out.println("Your insurance is " + premium);

        // Logic Error: Closing scanner before reading email
        reader.close();

        System.out.print("Enter your email: ");
        String email = reader.next(); // This will throw IllegalStateException
        if (email.indexOf("@") >= 0) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
