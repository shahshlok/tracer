import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner inputScanner = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numeric_grade = inputScanner.nextInt();

    int grade_copy = numeric_grade;

    
    if (grade_copy < 0) {
      grade_copy = 0;
    } else if (grade_copy > 100) {
        grade_copy = 100;
    }

		char letterGrade = 'F';

    if (grade_copy >= 90 && grade_copy <= 100) {
        letterGrade = 'A';
    } else if (grade_copy >= 80 && grade_copy <= 89) {
      letterGrade = 'B';
    } else if (grade_copy >= 70 && grade_copy <= 79) {
      		letterGrade = 'C';
    } else if (grade_copy >= 60 && grade_copy <= 69) {
      letterGrade = 'D';
    } else {
      if (grade_copy >= 0 && grade_copy < 60) {
        letterGrade = 'F';
      }
    }

    char finalLetter = letterGrade;

      System.out.println("Letter grade: " + finalLetter);

    inputScanner.close();
  }
}