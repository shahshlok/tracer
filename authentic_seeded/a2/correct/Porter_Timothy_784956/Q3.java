import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {

   Scanner input_scanner = new Scanner(System.in);

   System.out.print("Enter grade: ");
      int numericGrade = input_scanner.nextInt();

      int safe_grade = numericGrade;

      if (safe_grade < 0) {
         safe_grade = 0;
      }
      if (safe_grade > 100) {
         safe_grade = 100;
      }

		char letter_grade = 'F';

      if (safe_grade >= 90 && safe_grade <= 100) {
        letter_grade = 'A';
      } else if (safe_grade >= 80 && safe_grade <= 89) {
           letter_grade = 'B';
      } else if (safe_grade >= 70 && safe_grade <= 79) {
    		  letter_grade = 'C';
      } else if (safe_grade >= 60 && safe_grade <= 69) {
         letter_grade = 'D';
      } else {
         if (safe_grade < 60) {
            letter_grade = 'F';
         }
      }


      if (letter_grade != 0) {
        System.out.println("Letter grade: " + letter_grade);
      }

      input_scanner.close();
  }
}