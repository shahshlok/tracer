import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input_scanner = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numericGrade = input_scanner.nextInt();



      int temp_holder = numericGrade;
   char letter_grade = 'F';

	 if (temp_holder >= 0 || temp_holder <= 100) {

      if (temp_holder >= 90 && temp_holder <= 100) {
        letter_grade = 'A';
      } else if (temp_holder >= 80 && temp_holder <= 89) {
          letter_grade = 'B';
      } else if (temp_holder >= 70 && temp_holder <= 79) {
      	letter_grade = 'C';
      } else if (temp_holder >= 60 && temp_holder <= 69) {
           letter_grade = 'D';
      } else if (temp_holder < 60 && temp_holder >= 0) {
            letter_grade = 'F';
      }
	 }


    System.out.println("Letter grade: " + letter_grade);

    input_scanner.close();
  }
}