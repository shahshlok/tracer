import java.util.Scanner;

public class InsuranceTony {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter age and number of accidents: ");
        String line = scan.nextLine();
        String[] parts = line.split(" ");
        int age = Integer.parseInt(parts[0]);
        int acc = Integer.parseInt(parts[1]);

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double amount = 600;
        if (age < 24) {
            amount += 200;
        }
        if (acc > 0) {
            amount += amount * 0.25;
        }

        System.out.println("Your insurance is " + amount);
        System.out.print("Enter your email: ");
        String email = scan.nextLine();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
