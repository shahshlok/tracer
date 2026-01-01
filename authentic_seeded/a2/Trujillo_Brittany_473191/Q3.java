import java.util.Scanner;

public class Q3 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int grade_value = input.nextInt();

      
      char letterGrade;

      	if (grade_value >= 90) {
      		System.out.println("Letter grade: A");
      	}
      	if (grade_value >= 80) {
        System.out.println("Letter grade: B");
      	}
      	if (grade_value >= 70) {
          System.out.println("Letter grade: C");
      	}
      	if (grade_value >= 60) {
  	     System.out.println("Letter grade: D");
      	}
      	if (grade_value < 60) {
      System.out.println("Letter grade: F");
      	}

      
      input.close();
   }
}