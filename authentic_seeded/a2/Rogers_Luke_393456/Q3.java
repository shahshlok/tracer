import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();
        int tempGrade = grade;

        String letterGrade = "";
        String tempLetter = "";

        if (tempGrade >= 90 && tempGrade <= 100) {
            tempLetter = "A";
        } else if (tempGrade >= 80 && tempGrade <= 89) {
            tempLetter = "B";
        } else if (tempGrade >= 70 && tempGrade <= 79) {
            tempLetter = "C";
        } else if (tempGrade >= 60 && tempGrade <= 69) {
            tempLetter = "D";
        } else if (tempGrade >= 0 && tempGrade < 60) {
            tempLetter = "F";
        } else {
            tempLetter = "Invalid";
        }

        letterGrade = tempLetter;

        if (!letterGrade.equals("Invalid")) {
            System.out.println("Letter grade: " + letterGrade);
        } else {
            System.out.println("Letter grade: Invalid");
        }

        scanner.close();
    }
}