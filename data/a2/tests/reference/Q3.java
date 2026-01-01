import java.util.Scanner;

public class Q3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int grade = scanner.nextInt();
        
        String letter;
        if (grade >= 90) {
            letter = "A";
        } else if (grade >= 80) {
            letter = "B";
        } else if (grade >= 70) {
            letter = "C";
        } else if (grade >= 60) {
            letter = "D";
        } else {
            letter = "F";
        }
        
        System.out.println("Letter grade: " + letter);
        scanner.close();
    }
}
