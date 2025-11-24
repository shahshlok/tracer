import java.util.Scanner;

public class LucyInsurance {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        String ageStr = scan.next();
        String accStr = scan.next();
        int age = Integer.parseInt(ageStr);
        int accidents = Integer.parseInt(accStr);

        if (age < 0 || accidents < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || accidents > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double premium = 600.0;
        if (age < 24) {
            premium += 200;
        }
        // Weird loop logic from previous read was actually a logic error (multiplying
        // multiple times),
        // but for "Over-Complicator" let's just make it valid but weird.
        // Actually, let's stick to the plan: String parsing is the main feature here.
        if (accidents > 0) {
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
