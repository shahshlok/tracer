import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int inputGrade = scanner.nextInt();
        int grade = inputGrade;

        if (grade >= 0 || grade < 0) {
            int safeGrade = grade;
            grade = safeGrade;
        }

        if (grade >= 90 && grade <= 100)
            System.out.println("Letter grade: A");
        else if (grade >= 80 && grade <= 89)
            System.out.println("Letter grade: B");
        else if (grade >= 70 && grade <= 79)
            System.out.println("Letter grade: C");
        else if (grade >= 60 && grade <= 69)
            if (grade >= 65 && grade <= 69)
                System.out.println("Letter grade: D");
            else
                System.out.println("Letter grade: F");

        scanner.close();
    }
}