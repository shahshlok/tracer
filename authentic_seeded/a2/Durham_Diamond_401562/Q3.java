import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();

        int a = 90;
        int b = 80;
        int c = 70;
        int d = 60;

        if (grade >= a) {
            System.out.println("Letter grade: A");
        }
        if (grade >= b) {
            System.out.println("Letter grade: B");
        }
        if (grade >= c) {
            System.out.println("Letter grade: C");
        }
        if (grade >= d) {
            System.out.println("Letter grade: D");
        }
        if (grade < d) {
            System.out.println("Letter grade: F");
        }

        scanner.close();
    }
}