import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numeric_grade = sc.nextInt();

    
    int temp_grade = numeric_grade;

    if (temp_grade < 0) {
       temp_grade = 0;
    }
    if (temp_grade > 100) {
      temp_grade = 100;
    }

    char letterGrade = 'F';

      if (temp_grade >= 90 && temp_grade <= 100) {
      	letterGrade = 'A';
      } else if (temp_grade >= 80 && temp_grade <= 89) {
        letterGrade = 'B';
      } else if (temp_grade >= 70 && temp_grade <= 79) {
      	  letterGrade = 'C';
      } else if (temp_grade >= 60 && temp_grade <= 69) {
      	letterGrade = 'D';
      } else {
      	if (temp_grade < 60) {
      		letterGrade = 'F';
      	}
      }

      System.out.println("Letter grade: " + letterGrade);
  }
}