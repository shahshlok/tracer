import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {

      Scanner input  = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int numeric_grade = input.nextInt();


      int a = 90;
   int b = 80;
		int c = 70;
   int d = 60;



      char letterGrade;

      if (numeric_grade >= a && numeric_grade <= 100) {
         letterGrade = 'A';
      } else if (numeric_grade >= b) {
      	  letterGrade = 'B';
      } else if (numeric_grade >= c) {
	 letterGrade = 'C';
      } else if (numeric_grade >= d) {
      	   letterGrade = 'D';
      } else {
	letterGrade = 'F';
      }

      System.out.println("Letter grade: " + letterGrade);
   }
}