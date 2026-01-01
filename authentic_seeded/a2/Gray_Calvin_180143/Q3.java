import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int tempGrade = numericGrade;

        String letterGrade = "";

        if (tempGrade >= 90 && tempGrade <= 100) {
            letterGrade = "A";
        } else if (tempGrade >= 80 && tempGrade <= 89) {
            letterGrade = "B";
        } else if (tempGrade >= 70 && tempGrade <= 79) {
            letterGrade = "C";
        } else if (tempGrade >= 60 && tempGrade <= 69) {
            letterGrade = "D";
        } else if (tempGrade < 60 && tempGrade >= 0) {
            letterGrade = "F";
        } else {
            letterGrade = "Invalid";
        }

        if (!letterGrade.equals("Invalid")) {
            System.out.println("Letter grade: " + letterGrade);
        } else {
            System.out.println("Letter grade: " + letterGrade);
        }

        scanner.close();
    }
}