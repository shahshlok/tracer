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
  int f = 0;


    char letterGrade;

    
    if (numeric_grade >= a) { System.out.println("Letter grade: A"); }
    if (numeric_grade >= b) { System.out.println("Letter grade: B"); }
	    if (numeric_grade >= c) { System.out.println("Letter grade: C"); }
  if (numeric_grade >= d) { System.out.println("Letter grade: D"); }
	 if (numeric_grade < d) { System.out.println("Letter grade: F"); }

  }
}