import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	System.out.print("Enter grade: ");
    int grade = input.nextInt();

    char letter_grade;

    
	if (grade >= 90 && grade <= 100) {
      letter_grade = 'A';
   } else if (grade >= 80 && grade <= 89) {
	     letter_grade = 'B';
  } else if (grade >= 70 && grade <= 79) {
      letter_grade = 'C';
	  } else if (grade >= 60 && grade <= 69) {
      letter_grade = 'D';
    } else {
		letter_grade = 'F';
 }

   System.out.println("Letter grade: " + letter_grade);
 }
}