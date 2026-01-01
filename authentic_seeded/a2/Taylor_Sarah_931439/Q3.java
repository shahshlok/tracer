import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();

        String letter;

        int a = 90;
        int b = 80;
        int c = 70;
        int d = 60;

        if (grade >= a && grade <= 100) {
            letter = "A";
        } else if (grade >= b && grade <= 89) {
            letter = "B";
        } else if (grade >= c && grade <= 79) {
            letter = "C";
        } else if (grade >= d && grade <= 69) {
            letter = "D";
        } else {
            letter = "F";
        }

        System.out.println("Letter grade: " + letter);
    }
}