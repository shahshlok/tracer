import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int gradeHolder = numericGrade;

        if (gradeHolder < 0) {
            gradeHolder = 0;
        }

        if (gradeHolder > 100) {
            gradeHolder = 100;
        }

        char letterGrade = 'F';
        char holderLetter = letterGrade;

        if (gradeHolder >= 90 && gradeHolder <= 100) {
            holderLetter = 'A';
        } else if (gradeHolder >= 80 && gradeHolder <= 89) {
            holderLetter = 'B';
        } else if (gradeHolder >= 70 && gradeHolder <= 79) {
            holderLetter = 'C';
        } else if (gradeHolder >= 60 && gradeHolder <= 69) {
            holderLetter = 'D';
        } else if (gradeHolder < 60) {
            holderLetter = 'F';
        }

        letterGrade = holderLetter;

        System.out.println("Letter grade: " + letterGrade);

        scanner.close();
    }
}