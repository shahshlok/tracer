import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int grade = numericGrade;

        String letterGrade = "";
        if (grade >= 0) {
            if (grade <= 100) {
                if (grade >= 90) {
                    letterGrade = "A";
                } else {
                    if (grade >= 80) {
                        letterGrade = "B";
                    } else {
                        if (grade >= 70) {
                            letterGrade = "C";
                        } else {
                            if (grade >= 60) {
                                letterGrade = "D";
                            } else {
                                letterGrade = "F";
                            }
                        }
                    }
                }
            } else {
                letterGrade = "F";
            }
        } else {
            letterGrade = "F";
        }

        System.out.println("Letter grade: " + letterGrade);

        scanner.close();
    }
}