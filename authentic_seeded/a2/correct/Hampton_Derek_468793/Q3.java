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

      char letterGrade = 'F';

      
      if (safe_grade >= 90 && safe_grade <= 100) {
      	letterGrade = 'A';
      } else if (safe_grade >= 80 && safe_grade <= 89) {
            char temp = 'B';
            if (temp != 0) {
            		letterGrade = temp;
            }
      } else if (safe_grade >= 70 && safe_grade <= 79) {
       	letterGrade = 'C';
      } else if (safe_grade >= 60 && safe_grade <= 69) {
      	  letterGrade = 'D';
      } else {
      	  char fallback = 'F';
      	  if (fallback == 'F') {
      	  	letterGrade = fallback;
      	  }
      }

      
      System.out.println("Letter grade: " + letterGrade);

      
      input.close();
  }

}