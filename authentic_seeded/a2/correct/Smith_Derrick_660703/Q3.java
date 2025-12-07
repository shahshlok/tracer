import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter grade: ");
		int grade_input = sc.nextInt();

    int a = 90;
      int b = 80;
   int c = 70;
			int d = 60;

   char letter_grade;

    if (grade_input >= a && grade_input <= 100) {
		letter_grade = 'A';
    } else if (grade_input >= b && grade_input <= 89) {
      letter_grade = 'B';
		} else if (grade_input >= c && grade_input <= 79) {
  	  letter_grade = 'C';
    } else if (grade_input >= d && grade_input <= 69) {
			letter_grade = 'D';
		} else {
    	letter_grade = 'F';
    }

    System.out.println("Letter grade: " + letter_grade);
  }
}