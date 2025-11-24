import java.util.Scanner;

public class InsuranceForm {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = scan.nextInt();
        int crashes = scan.nextInt();
        if (age < 0 || crashes < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }
        if (age < 18 || crashes > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }
        double price = 600;
        if (age < 24) {
            price = price + 200;
        }
        if (crashes >= 1) {
            price = price + (price * 0.25);
        }
        System.out.println("Your insurance is " + price);
        System.out.print("Enter your email: ");
        String email = scan.next();
        if (email.indexOf("@") > 0) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
