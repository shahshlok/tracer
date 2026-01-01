import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input_scanner = new Scanner(System.in);

      System.out.print("Enter grade: ");
      
      int numericGrade = input_scanner.nextInt();
      
      
      int tempGradeHolder = numericGrade;

      if (tempGradeHolder < 0) {
         tempGradeHolder = 0;
      }

      if (tempGradeHolder > 100) {
         tempGradeHolder = 100;
      }

      char letter_grade = 'F';

      if (tempGradeHolder >= 90 && tempGradeHolder <= 100) {
        letter_grade = 'A';
      } else if (tempGradeHolder >= 80 && tempGradeHolder <= 89) {
          letter_grade = 'B';
      } else if (tempGradeHolder >= 70 && tempGradeHolder <= 79) {
      	 letter_grade = 'C';
      } else if (tempGradeHolder >= 60 && tempGradeHolder <= 69) {
      	  letter_grade = 'D';
      } else {
      	  if (tempGradeHolder < 60) {
      	     letter_grade = 'F';
      	  }
      }

      
      System.out.println("Letter grade: " + letter_grade);

      input_scanner.close();
   }
}