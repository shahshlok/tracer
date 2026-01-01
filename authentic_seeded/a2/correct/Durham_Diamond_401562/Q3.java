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

        char letter;

        if (grade >= a && grade <= 100) {
            letter = 'A';
        } else if (grade >= b) {
            letter = 'B';
        } else if (grade >= c) {
            letter = 'C';
        } else if (grade >= d) {
            letter = 'D';
        } else {
            letter = 'F';
        }

        System.out.println("Letter grade: " + letter);
        scanner.close();
    }
}