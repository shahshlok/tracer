import java.util.Scanner;

public class Q3 {

  public static void main(String[] args) {

   Scanner input_scanner = new Scanner(System.in);

   System.out.print("Enter grade: ");
      int numericGrade = input_scanner.nextInt();

      int safe_grade = numericGrade;

      if (safe_grade < 0)
         safe_grade = 0;

      if (safe_grade > 100)
         safe_grade = 100;

		int grade = safe_grade;



      if (grade >= 90)
        System.out.println("Letter grade: A");
      else if (grade >= 80)
           System.out.println("Letter grade: B");
      else if (grade >= 70)
    		  System.out.println("Letter grade: C");
      else if (grade >= 60)
         if (grade >= 65)
            System.out.println("Letter grade: D");
         else
            System.out.println("Letter grade: F");



      input_scanner.close();
  }
}