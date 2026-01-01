import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
	   
      Scanner input_scanner = new Scanner(System.in);

      System.out.print("Enter grade: ");
      
      int numericGrade = input_scanner.nextInt();
      int tempGradeHolder = numericGrade;

      if (tempGradeHolder < 0) {
         tempGradeHolder = 0;
      }
      if (tempGradeHolder > 100) {
          tempGradeHolder = 100;
      }

      int grade = tempGradeHolder;

      if (grade >= 90) { System.out.println("Letter grade: A"); }
      if (grade >= 80) { System.out.println("Letter grade: B"); }
      if (grade >= 70) { System.out.println("Letter grade: C"); }
      if (grade >= 60) { System.out.println("Letter grade: D"); }
      if (grade < 60)  { System.out.println("Letter grade: F"); }

      input_scanner.close();
   }
}