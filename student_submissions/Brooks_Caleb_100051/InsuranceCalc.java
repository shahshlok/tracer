import java.util.Scanner;

public class InsuranceCalc {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter age and number of accidents:");
        int age = in.nextInt();
        int accidents = in.nextInt();
        if (age < 0 || accidents < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }
        if (age <= 17 || accidents > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }
        int cost = 600;
        if (age < 24) {
            cost = cost + 200;
        }
        if (accidents > 0) {
            cost = cost + (int)(600 * 0.25);
        }
        System.out.println("Your insurance is " + cost);
        System.out.print("Enter your email: ");
        string email = in.next(); // Lowercase string
        if (email.indexOf("@") >= 0) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
// Missing closing brace
