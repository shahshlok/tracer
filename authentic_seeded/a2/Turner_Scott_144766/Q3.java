import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int numericGrade = scanner.nextInt();
        int grade = numericGrade;

        if (grade >= 0 || grade <= 100) {
            int holderGrade = grade;
            if (holderGrade >= 90)
                System.out.println("Letter grade: A");
            else if (holderGrade >= 80)
                System.out.println("Letter grade: B");
            else if (holderGrade >= 70)
                System.out.println("Letter grade: C");
            else if (holderGrade >= 60)
                if (holderGrade >= 65)
                    System.out.println("Letter grade: D");
                else
                    System.out.println("Letter grade: F");
        }

        scanner.close();
    }
}