import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

   System.out.print("Enter grade: ");
    int numeric_grade = input.nextInt();

    
    char letterGrade;

   if (numeric_grade >= 90 && numeric_grade <= 100) {
      letterGrade = 'A';
   } else if (numeric_grade >= 80 && numeric_grade <= 89) {
    		letterGrade = 'B';
    } else if (numeric_grade >= 70 && numeric_grade <= 79) {
      letterGrade = 'C';
    } else if (numeric_grade >= 60 && numeric_grade <= 69) {
      		letterGrade = 'D';
    } else {
      letterGrade = 'F';
    }

    
    System.out.println("Letter grade: " + letterGrade);
  }

}