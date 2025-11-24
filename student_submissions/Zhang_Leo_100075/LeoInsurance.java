import java.util.Scanner;

public class LeoInsurance {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = input.nextInt();
        int accidents = input.nextInt();

        if (age < 0 || accidents < 0) {
            System.out.println("Invalid age or number of accidents.");
        }

        if (age < 18 || accidents > 2) {
            System.out.println("You are not eligible for insurance.");
            return;
        }

        double price = 600.0;
        if (age < 24) {
            price = price + 200;
        }
        if (accidents > 0) {
            price = price * 1.25;
        }

        System.out.println("Your insurance is " + price);
        System.out.print("Enter your email: ");
        String mail = input.next();
        if (mail.indexOf("@") > -1) {
            System.out.println("quote will be sent to " + mail);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
