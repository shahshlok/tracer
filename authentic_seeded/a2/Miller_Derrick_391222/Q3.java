import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner sc  = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int grade = sc.nextInt();



      int a = 90;
   	int b = 80;
      int c = 70;
      		int d = 60;

      char letter_grade;

      if (grade >= a && grade <= 100) {
      	letter_grade = 'A';
      } else if (grade >= b && grade <= 89) {
         letter_grade = 'B';
      } else if (grade >= c && grade <= 79) {
      		letter_grade = 'C';
      } else if (grade >= d && grade <= 69) {
      	  letter_grade = 'D';
      } else {
      	letter_grade = 'F';
      }

      System.out.println("Letter grade: " + letter_grade);
   }
}