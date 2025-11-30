import java.util.Scanner;

public class Q1 {
// TODO: Clean up code before submission
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter v0, v1, and t: ");
        double v0 = scan.nextDouble();
        double v1 = scan.nextDouble();
        double a = (v1 - v0) / 1;
        System.out.println("The average acceleration is " + a);
    }
}