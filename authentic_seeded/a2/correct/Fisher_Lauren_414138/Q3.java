import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner scanner   = new Scanner(System.in);

      System.out.print("Enter grade: ");
	int numeric_grade = scanner.nextInt();

    int tempGrade = numeric_grade;

      if (tempGrade < 0) {
        tempGrade = 0;
      }

   if (tempGrade > 100) {
      tempGrade = 100;
   }

      char letterGrade = 'F';

	if (tempGrade >= 90 && tempGrade <= 100) {
      letterGrade = 'A';
   } else if (tempGrade >= 80 && tempGrade <= 89) {
	      letterGrade = 'B';
  } else if (tempGrade >= 70 && tempGrade <= 79) {
      letterGrade = 'C';
     } else if (tempGrade >= 60 && tempGrade <= 69) {
	      letterGrade = 'D';
	    } else {
      if (tempGrade < 60) {
          letterGrade = 'F';
      }
  }

      char result_holder = letterGrade;

 System.out.println("Letter grade: " + result_holder);

      scanner.close();
 }
}