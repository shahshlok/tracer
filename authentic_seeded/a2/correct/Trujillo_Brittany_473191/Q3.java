import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int grade_value = input.nextInt();

      
      char letterGrade;

      	if (grade_value >= 90 && grade_value <= 100) {
      		letterGrade = 'A';
      	} else if (grade_value >= 80 && grade_value <= 89) {
        letterGrade = 'B';
      	} else if (grade_value >= 70 && grade_value <= 79) {
          letterGrade = 'C';
      	} else if (grade_value >= 60 && grade_value <= 69) {
  	     letterGrade = 'D';
      	} else {
      letterGrade = 'F';
      	}

      
      System.out.println("Letter grade: " + letterGrade);
      input.close();
   }
}