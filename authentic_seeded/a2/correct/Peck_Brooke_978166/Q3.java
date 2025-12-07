import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int holderGrade = numericGrade;

        String letterGrade = "";
        String holderLetter = "";

        if (holderGrade >= 90 && holderGrade <= 100) {
            holderLetter = "A";
        } else if (holderGrade >= 80 && holderGrade <= 89) {
            holderLetter = "B";
        } else if (holderGrade >= 70 && holderGrade <= 79) {
            holderLetter = "C";
        } else if (holderGrade >= 60 && holderGrade <= 69) {
            holderLetter = "D";
        } else {
            holderLetter = "F";
        }

        if (holderLetter != null) {
            letterGrade = holderLetter;
        }

        System.out.println("Letter grade: " + letterGrade);

        scanner.close();
    }
}