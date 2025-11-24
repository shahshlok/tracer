import java.util.Scanner;

public class QuoteMaker {
    public static void main(String[] args) {
        // Bad Practice: Creating new Scanner for every input
        System.out.print("Enter age and number of accidents: ");
        int age = new Scanner(System.in).nextInt();
        int accidents = new Scanner(System.in).nextInt();

        if (age < 0 || accidents < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (age < 18 || accidents > 2) {
            System.out.println("You are not eligible for insurance.");
        } else {
            double cost = 600;
            if (age < 24) {
                cost = cost + 200;
            }
            if (accidents > 0) {
                cost = cost + cost * 0.25;
            }
            System.out.println("Your insurance is " + cost);
        }

        System.out.print("Enter your email: ");
        String mail = new Scanner(System.in).next();
        if (mail.contains("@")) {
            System.out.println("quote will be sent to " + mail);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
