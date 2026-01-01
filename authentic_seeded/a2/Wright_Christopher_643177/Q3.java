import java.util.Scanner;

public class Q3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter grade: ");
        int inputGrade = scanner.nextInt();
        int grade = inputGrade;

        if (grade >= 0 || grade < 0) {
            if (grade >= 90)
                System.out.println("Letter grade: A");
            else if (grade >= 80)
                System.out.println("Letter grade: B");
            else if (grade >= 70)
                System.out.println("Letter grade: C");
            else if (grade >= 60)
                if (grade >= 65)
                    System.out.println("Letter grade: D");
                else
                    System.out.println("Letter grade: F");
        }

        scanner.close();
    }
}