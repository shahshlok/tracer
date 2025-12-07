import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numeric_grade = input.nextInt();


      int a = 90;
   int b = 80;
      int c = 70;
   int d = 60;



      char letter_grade;

    if (numeric_grade >= a && numeric_grade <= 100) {
      letter_grade = 'A';
    } else if (numeric_grade >= b && numeric_grade <= 89) {
    	  letter_grade = 'B';
    } else if (numeric_grade >= c && numeric_grade <= 79) {
      letter_grade = 'C';
    } else if (numeric_grade >= d && numeric_grade <= 69) {
        letter_grade = 'D';
    } else {
      letter_grade = 'F';
    }

    	System.out.println("Letter grade: " + letter_grade);
  }
}