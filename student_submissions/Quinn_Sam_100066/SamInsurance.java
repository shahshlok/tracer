import java.util.Scanner;

public class SamInsurance {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = scan.nextInt();
        int acc = scan.nextInt();

        if (age < 0 || acc < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }

        if (Calculator.isEligible(age, acc) == false) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double total = Calculator.calculate(age, acc);

        System.out.println("Your insurance is " + total);
        System.out.print("Enter your email: ");
        String email = scan.next();
        if (email.contains("@")) {
            System.out.println("quote will be sent to " + email);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }

    static class Calculator {
        public static boolean isEligible(int age, int acc) {
            return !(age < 18 || acc > 2);
        }

        public static double calculate(int age, int acc) {
            double base = 600;
            if (age < 24)
                base += 200;
            if (acc > 0)
                base += base * 0.25;
            return base;
        }
    }
}
