import java.util.Scanner;

public class Q3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int inputGrade = scanner.nextInt();
        int grade = inputGrade;

        String letterGrade = "";

        if (grade >= 90 && grade <= 100) {
            letterGrade = "A";
        } else {
            if (grade >= 80 && grade <= 89) {
                letterGrade = "B";
            } else {
                if (grade >= 70 && grade <= 79) {
                    letterGrade = "C";
                } else {
                    if (grade >= 60 && grade <= 69) {
                        letterGrade = "D";
                    } else {
                        letterGrade = "F";
                    }
                }
            }
        }

        if (letterGrade != null) {
            System.out.println("Letter grade: " + letterGrade);
        }

        scanner.close();
    }
}