import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {

   Scanner input_scanner = new Scanner(System.in);

	System.out.print("Enter grade: ");

      int raw_grade = input_scanner.nextInt();

      int grade = raw_grade;

      if (grade < 0) {
         grade = 0;
      }
      if (grade > 100) {
         grade = 100;
      }

      String letterGrade = "";

      int temp_grade_holder = grade;

      if (temp_grade_holder >= 90 && temp_grade_holder <= 100) {
          letterGrade = "A";
      } else if (temp_grade_holder >= 80 && temp_grade_holder <= 89) {
           letterGrade = "B";
      } else if (temp_grade_holder >= 70 && temp_grade_holder <= 79) {
      		letterGrade = "C";
      } else if (temp_grade_holder >= 60 && temp_grade_holder <= 69) {
           letterGrade = "D";
      } else {
        if (temp_grade_holder < 60) {
        	 letterGrade = "F";
        }
      }

      if (letterGrade != null) {
      	System.out.println("Letter grade: " + letterGrade);
      }

      input_scanner.close();
  }
}