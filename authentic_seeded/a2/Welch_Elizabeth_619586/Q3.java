import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int grade = input.nextInt();


      int a = 90;
   int b = 80;
		int c = 70;
   int d = 60;
      int zero = 0;
      int hundred = 100;

      if (grade < zero) {
         grade = zero;
      } else if (grade > hundred) {
         grade = hundred;
      }

      String letter_grade;

      if (grade >= a && grade <= hundred) {
		  letter_grade = "A";
      } else if (grade >= b && grade < a) {
   letter_grade = "B";
      } else if (grade >= c && grade < b) {
		  letter_grade = "C";
      } else if (grade >= d && grade < c) {
      letter_grade = "D";
      } else {
		  letter_grade = "F";
      }

      System.out.println("Letter grade: " + letter_grade);
   }
}