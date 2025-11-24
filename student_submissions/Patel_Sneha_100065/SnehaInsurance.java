import java.util.Scanner;

public class SnehaInsurance {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int[] data = new int[2];
        data[0] = scan.nextInt(); // Age
        data[1] = scan.nextInt(); // Accidents

        if (data[0] < 0 || data[1] < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (data[0] < 18 || data[1] > 2) {
            System.out.println("You are not eligible for insurance.");
            return; // Added return to match others, though original didn't have it, it's safer for
                    // this archetype to be "valid but weird"
        }

        double premium = 600;
        if (data[0] < 24) {
            premium += 200;
        }
        if (data[1] > 0) {
            premium += premium * 0.25;
        }

        System.out.println("Your insurance is " + premium);
        System.out.print("Enter your email: ");
        String email = scan.next();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
