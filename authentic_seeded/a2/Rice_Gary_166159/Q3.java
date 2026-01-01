import java.util.Scanner;

public class Q3 {

   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter grade: ");
      int grade_value = input.nextInt();



      if (grade_value >= 90)
         System.out.println("Letter grade: A");
      else if (grade_value >= 80)
      	  System.out.println("Letter grade: B");
      else if (grade_value >= 70)
		  System.out.println("Letter grade: C");
      else if (grade_value >= 60)
         if (grade_value >= 65)
      	    System.out.println("Letter grade: D");
         else
      	    System.out.println("Letter grade: F");
   }
}