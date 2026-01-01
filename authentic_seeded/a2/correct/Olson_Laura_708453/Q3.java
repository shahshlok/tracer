import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
  	Scanner input_scanner = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numericGrade = input_scanner.nextInt();

    int safeGrade = numericGrade;
    if (safeGrade < 0) {
      safeGrade = 0;
    }
    if (safeGrade > 100) {
       safeGrade = 100;
    }

    char letter_grade = 'F';

    
    if (safeGrade >= 90 && safeGrade <= 100) {
      		letter_grade = 'A';
    } else if (safeGrade >= 80 && safeGrade <= 89) {
      letter_grade = 'B';
    } else if (safeGrade >= 70 && safeGrade <= 79) {
      	letter_grade = 'C';
    } else if (safeGrade >= 60 && safeGrade <= 69) {
      letter_grade = 'D';
    } else {
      if (safeGrade < 60) {
      	letter_grade = 'F';
      }
    }

    char finalLetter = letter_grade;
    System.out.println("Letter grade: " + finalLetter);

    input_scanner.close();
  }
}