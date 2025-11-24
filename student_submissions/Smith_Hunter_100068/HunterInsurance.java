import java.util.Scanner;

public class HunterInsurance {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = s.nextInt();
        int accidents = s.nextInt();

        if (age < 0 || accidents < 0) {
            System.out.println("Invalid age or number of accidents.");
        } else {
            if (age < 18) {
                System.out.println("You are not eligible for insurance.");
            } else {
                if (accidents > 2) {
                    System.out.println("You are not eligible for insurance.");
                } else {
                    double premium = 600;
                    if (age < 24) {
                        premium = premium + 200;
                        if (accidents > 0) {
                            premium = premium + 0.25 * premium; // Note: logic here is slightly different order but
                                                                // mathematically same if done right
                        }
                    } else {
                        if (accidents > 0) {
                            premium = premium + 0.25 * premium;
                        }
                    }
                    // Wait, the logic above in the nested block is actually duplicated/messy.
                    // If age < 24, add 200, then check accidents.
                    // If age >= 24, just check accidents.
                    // This is correct but "nested hell".

                    System.out.println("Your insurance is " + premium);
                    System.out.print("Enter your email: ");
                    String email = s.next();
                    if (email.indexOf("@") > -1) {
                        System.out.println("quote will be sent to " + email);
                    } else {
                        System.out.println("Invalid email. Can't send you the quote.");
                    }
                }
            }
        }
    }
}
