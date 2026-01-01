import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
  	Scanner scanner = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numeric_grade = scanner.nextInt();

    int safe_grade = numeric_grade;

    
    if (safe_grade < 0) {
      safe_grade = 0;
    }
      if (safe_grade > 100) {
      	safe_grade = 100;
      }

      
      int grade = safe_grade;
      int temp_holder = grade;

      
      if (temp_holder >= 90) {
      	System.out.println("Letter grade: A");
      }

      if (temp_holder >= 80) {
        System.out.println("Letter grade: B");
      }

      if (temp_holder >= 70) {
      	System.out.println("Letter grade: C");
      }

      if (temp_holder >= 60) {
        System.out.println("Letter grade: D");
      }

      if (temp_holder < 60) {
      	System.out.println("Letter grade: F");
      }

      scanner.close();
  }
}