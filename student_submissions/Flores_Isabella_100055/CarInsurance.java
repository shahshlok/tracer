import java.util.Scanner; // Fixed the missing import from before, but introducing logic error now

public class CarInsurance {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = scan.nextInt();
        int accident = scan.nextInt();
        if (age < 0 || accident < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }
        // Logic Error: Used && instead of ||. Only ineligible if BOTH under 18 AND > 2
        // accidents.
        if (age <= 17 && accident > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }
        double base = 600;
        if (age < 24) {
            base = base + 200;
        }
        if (accident > 0) {
            base = base * 1.25;
        }
        System.out.println("Your insurance is " + base);
        System.out.print("Enter your email: ");
        String email = scan.next();
        if (email.indexOf("@") > -1) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
