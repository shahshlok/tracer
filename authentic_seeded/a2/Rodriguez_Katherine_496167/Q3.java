import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();

        if (grade >= 90) {
            System.out.println("Letter grade: A");
        }
        if (grade >= 80) {
            System.out.println("Letter grade: B");
        }
        if (grade >= 70) {
            System.out.println("Letter grade: C");
        }
        if (grade >= 60) {
            System.out.println("Letter grade: D");
        }
        if (grade < 60) {
            System.out.println("Letter grade: F");
        }

        scanner.close();
    }
}