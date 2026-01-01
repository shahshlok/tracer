import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter grade: ");
    int numeric_grade = input.nextInt();

    if (numeric_grade >= 90) {
      System.out.println("Letter grade: A");
    }
    if (numeric_grade >= 80) {
    	    System.out.println("Letter grade: B");
    }
    if (numeric_grade >= 70) {
      System.out.println("Letter grade: C");
    }
    if (numeric_grade >= 60) {
	    	System.out.println("Letter grade: D");
    }
    if (numeric_grade < 60) {
      System.out.println("Letter grade: F");
    }
  }
}