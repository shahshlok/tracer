import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numeric_grade = sc.nextInt();

    
      int grade_holder = numeric_grade;

      if (grade_holder < 0) {
      	 grade_holder = 0;
      }

      if (grade_holder > 100) {
         grade_holder = 100;
      }

      char letterGrade = 'F';


      if (grade_holder >= 90 && grade_holder <= 100) {
      	  letterGrade = 'A';
      } else if (grade_holder >= 80 && grade_holder <= 89) {
      	  letterGrade = 'B';
      } else if (grade_holder >= 70 && grade_holder <= 79) {
      	  letterGrade = 'C';
      } else if (grade_holder >= 60 && grade_holder <= 69) {
      	  letterGrade = 'D';
      } else {
      	  if (grade_holder < 60) {
      	  	 letterGrade = 'F';
      	  }
      }

      if (letterGrade != 0) {
      	  System.out.println("Letter grade: " + letterGrade);
      }

      sc.close();
  }
}