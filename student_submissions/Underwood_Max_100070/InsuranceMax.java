import java.util.Scanner;

public class InsuranceMax {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = sc.nextInt();
        int acc = sc.nextInt();

        boolean badInput = age < 0 || acc < 0;
        if (badInput) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || acc > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double pay = 600;
        if (age < 24) {
            pay += 200;
        }
        if (acc > 0) {
            pay = pay + (pay * 0.25);
        }

        System.out.println("Your insurance is " + pay);
        System.out.print("Enter your email: ");
        // Logic Error: nextInt() leaves newline, nextLine() consumes it immediately
        String email = sc.nextLine();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
