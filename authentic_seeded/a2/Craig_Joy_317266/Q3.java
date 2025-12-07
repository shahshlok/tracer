import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {

   Scanner scanner_obj = new Scanner(System.in);

   System.out.print("Enter grade: ");

   int numericGrade = scanner_obj.nextInt();

   int grade_copy = numericGrade;

   if (grade_copy < 0) {
      grade_copy = 0;
   }

   	if (grade_copy > 100) {
      grade_copy = 100;
   }

   char letter_grade = 'F';

   
   if (grade_copy >= 90 && grade_copy <= 100) {
      letter_grade = 'A';
   } else if (grade_copy >= 80 && grade_copy <= 89) {
       	letter_grade = 'B';
   } else if (grade_copy >= 70 && grade_copy <= 79) {
      letter_grade = 'C';
   } else if (grade_copy >= 60 && grade_copy <= 69) {
       	letter_grade = 'D';
   } else {
      if (grade_copy < 60) {
         letter_grade = 'F';
      }
   }

   if (letter_grade != 0) {
      System.out.println("Letter grade: " + letter_grade);
   }

   scanner_obj.close();
  }
}