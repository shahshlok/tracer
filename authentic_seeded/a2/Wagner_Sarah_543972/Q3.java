import java.util.Scanner;

public class Q3 {
  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter grade: ");
    int numeric_grade = input.nextInt();



    if (numeric_grade >= 90)
      System.out.println("Letter grade: A");
    else if (numeric_grade >= 80)
      	System.out.println("Letter grade: B");
    else if (numeric_grade >= 70)
      System.out.println("Letter grade: C");
    else if (numeric_grade >= 60)
      if (numeric_grade >= 65)
      	  System.out.println("Letter grade: D");
      else
      	System.out.println("Letter grade: F");
  }
}