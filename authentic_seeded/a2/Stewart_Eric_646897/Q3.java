import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int grade = numericGrade;

        if (grade < 0) {
            grade = 0;
        }

        if (grade > 100) {
            grade = 100;
        }

        String letter = "";
        if (grade >= 90 && grade <= 100) {
            letter = "A";
        } else if (grade >= 80 && grade <= 89) {
            letter = "B";
        } else if (grade >= 70 && grade <= 79) {
            letter = "C";
        } else if (grade >= 60 && grade <= 69) {
            letter = "D";
        } else {
            letter = "F";
        }

        if (letter != null) {
            System.out.println("Letter grade: " + letter);
        }

        scanner.close();
    }
}