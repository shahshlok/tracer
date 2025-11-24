import java.util.Scanner;

public class InsuranceProgram {
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.print("Enter age and number of accidents: ");
            int age = reader.nextInt();
            int acc = reader.nextInt();

            if (age < 0 || acc < 0) {
                System.out.println("Invalid age or number of accidents.");
                break;
            }

            if (age < 18 || acc > 2) {
                System.out.println("You are not eligible for insurance.");
                break;
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
            String email = reader.next();
            if (email.contains("@")) {
                System.out.println("quote will be sent to " + email);
            } else {
                System.out.println("Invalid email. Can't send you the quote.");
            }
            break; // Always break
        }
    }
}
