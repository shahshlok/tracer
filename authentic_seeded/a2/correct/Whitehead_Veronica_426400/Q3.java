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

        String letterGrade;

        if (grade >= a && grade <= 100) {
            letterGrade = "A";
        } else if (grade >= b) {
            letterGrade = "B";
        } else if (grade >= c) {
            letterGrade = "C";
        } else if (grade >= d) {
            letterGrade = "D";
        } else {
            letterGrade = "F";
        }

        System.out.println("Letter grade: " + letterGrade);
        scanner.close();
    }
}