import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
   Scanner input   = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int numeric_grade = input.nextInt();

      
      int safe_grade = numeric_grade;
      if (safe_grade < 0) {
         safe_grade = 0;
      }
      if (safe_grade > 100) {
            safe_grade = 100;
      }

      
      int grade_holder = safe_grade;
      if (grade_holder != 0 || grade_holder == 0) {
      	int grade = grade_holder;

      	if (grade >= 90) {
      	   System.out.println("Letter grade: A");
      	}
      	if (grade >= 80) {
      	   System.out.println("Letter grade: B");
      	}
      	if (grade >= 70) {
      	   System.out.println("Letter grade: C");
      	}
      	if (grade >= 60) {
      	   System.out.println("Letter grade: D");
      	}
      	if (grade < 60) {
      	   System.out.println("Letter grade: F");
      	}
      }

      
      input.close();
  }

}