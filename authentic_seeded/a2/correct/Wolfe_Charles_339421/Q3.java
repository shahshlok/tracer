import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {

   Scanner input_scanner = new Scanner(System.in);

   System.out.print("Enter grade: ");

      int numericGrade = input_scanner.nextInt();

      int temp_grade_holder = numericGrade;



      if (temp_grade_holder < 0) {
         temp_grade_holder = 0;
      }

      if (temp_grade_holder > 100) {
          temp_grade_holder = 100;
      }

   char letterGrade = 'F';

      if (temp_grade_holder >= 90 && temp_grade_holder <= 100) {
      letterGrade = 'A';
      } else if (temp_grade_holder >= 80 && temp_grade_holder <= 89) {
       letterGrade = 'B';
      } else if (temp_grade_holder >= 70 && temp_grade_holder <= 79) {
letterGrade = 'C';
      } else if (temp_grade_holder >= 60 && temp_grade_holder <= 69) {
	  		letterGrade = 'D';
      } else {
        if (temp_grade_holder < 60) {
           char tmp = 'F';
           letterGrade = tmp;
        }
      }

      if (letterGrade != 0) {
      System.out.println("Letter grade: " + letterGrade);
      }

      input_scanner.close();
  }
}