import java.util.Scanner;

public class InsuranceQuote {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter age and number of accidents: ");
        int age = sc.nextInt();
        int accidents = sc.nextInt();

        if(age < 0 || accidents < 0){
            System.out.println("Invalid age or number of accidents.");
            return // Missing semicolon
        }

        if(age < 18 || accidents > 2){
            system.out.println("You are not eligible for insurance."); // Lowercase system
            return;
        }

        double money = 600;
        if(age < 24){
            money += 200;
        }
        if(accidents > 0){
            money = money * 1.25;
        }

        System.out.println("Your insurance is " + money);
        System.out.print("Enter your email: ");
        String mail = sc.next();
        if(mail.indexOf("@") != -1){
            System.out.println("quote will be sent to " + mail);
        } else {
            System.out.println("Invalid email. Can't send you the quote.");
        }
    }
}
