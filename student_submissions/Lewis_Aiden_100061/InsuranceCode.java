import java.util.Scanner;

public class InsuranceCode {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int a = sc.nextInt();
        int b = sc.nextInt();

        if (a < 0 || b < 0) {
            System.out.println("Invalid age or number of accidents.");
            return;
        }
        if (a < 18 || b > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double c = 600;
        if (a < 24) {
            c += 200;
        }
        if (b > 0) {
            c += c * 0.25;
        }
        System.out.println("Your insurance is " + c);
        System.out.print("Enter your email: ");
        String d = sc.next();
        if (d.contains("@") == false)
            System.out.println("Invalid email. Can't send you the quote.");
        if (d.contains("@"))
            System.out.println("quote will be sent to " + d);
    }
}
