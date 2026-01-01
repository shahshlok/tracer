import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

    System.out.print("Enter grade: ");
	double  numeric_grade = input.nextDouble();


   double a = 90;
   double b = 80;
   double c = 70;
   double d = 60;
   double e = 0;

		char letterGrade;

  if (numeric_grade >= a && numeric_grade <= 100) {
      letterGrade = 'A';
   } else if (numeric_grade >= b && numeric_grade <= 89) {
	 letterGrade = 'B';
  }  else if (numeric_grade >= c && numeric_grade <= 79) {
      letterGrade = 'C';
	} else if (numeric_grade >= d && numeric_grade <= 69) {
   letterGrade = 'D';
	  } else {
   letterGrade = 'F';
 }

	System.out.println("Letter grade: " + letterGrade);
 }
}