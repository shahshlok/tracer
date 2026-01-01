import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter grade: ");
      
      int numeric_grade = sc.nextInt();
      
      
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
letterGrade = 'B';
         } else if (safe_grade >= 70 && safe_grade <= 79) {
         letterGrade = 'C';
         } else if (safe_grade >= 60 && safe_grade <= 69) {
            letterGrade = 'D';
         } else {
	    if (safe_grade < 60) {
	       letterGrade = 'F';
	    }
	 }

      char final_letter = letterGrade;
      
      if (final_letter != '\0') {
      System.out.println("Letter grade: " + final_letter);
      }

      sc.close();
   }

}