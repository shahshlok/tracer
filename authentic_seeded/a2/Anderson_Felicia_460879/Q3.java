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
   int e = 65;



      int grade = numeric_grade;


    if (grade >= a)
        System.out.println("Letter grade: A");
    else if (grade >= b)
      System.out.println("Letter grade: B");
    	else if (grade >= c)
        System.out.println("Letter grade: C");
	else if (grade >= d)
      if (grade >= e)
      	  System.out.println("Letter grade: D");
        else
      System.out.println("Letter grade: F");

  }
}