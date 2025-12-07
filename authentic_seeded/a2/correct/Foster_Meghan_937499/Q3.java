import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);
      System.out.print("Enter grade: ");
      
      int grade = input.nextInt();
      
	int letter_grade = grade;

      char letter;



      if (letter_grade >= 90 && letter_grade <= 100) {
         letter = 'A';
      } else if (letter_grade >= 80 && letter_grade <= 89) {
          letter = 'B';
      }  else if (letter_grade >= 70 && letter_grade <= 79) {
	   letter = 'C';
      } else if (letter_grade >= 60 && letter_grade <= 69) {
		  letter = 'D';
      } else {
        letter = 'F';
      }

      System.out.println("Letter grade: " + letter);
      
      input.close();
   }
}