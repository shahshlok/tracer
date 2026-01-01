import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int holderGrade = numericGrade;

        if (holderGrade < 0) {
            holderGrade = 0;
        }

        if (holderGrade > 100) {
            holderGrade = 100;
        }

        String letterGrade = "";

        if (holderGrade >= 90 && holderGrade <= 100) {
            letterGrade = "A";
        } else if (holderGrade >= 80 && holderGrade <= 89) {
            letterGrade = "B";
        } else if (holderGrade >= 70 && holderGrade <= 79) {
            letterGrade = "C";
        } else if (holderGrade >= 60 && holderGrade <= 69) {
            letterGrade = "D";
        } else {
            letterGrade = "F";
        }

        System.out.println("Letter grade: " + letterGrade);

        scanner.close();
    }
}