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

    char letter_grade = 'F';


    if (temp_grade_holder >= 90 && temp_grade_holder <= 100) {
      letter_grade = 'A';
    } else if (temp_grade_holder >= 80 && temp_grade_holder <= 89) {
      	letter_grade = 'B';
    } else if (temp_grade_holder >= 70 && temp_grade_holder <= 79) {
      letter_grade = 'C';
    } else if (temp_grade_holder >= 60 && temp_grade_holder <= 69) {
      	letter_grade = 'D';
    } else {
      if (temp_grade_holder < 60) {
        letter_grade = 'F';
      }
    }

    if (letter_grade != 0) {
      System.out.println("Letter grade: " + letter_grade);
    }

    input_scanner.close();
  }
}