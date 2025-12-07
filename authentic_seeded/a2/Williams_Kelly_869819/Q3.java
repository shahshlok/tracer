import java.util.Scanner;

public class Q3 {

	public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

   System.out.print("Enter grade: ");
		  int grade_input = sc.nextInt();


    int a = 90;
   int b = 80;
   		int c = 70;
  int d = 60;

   if (grade_input >= a) {
    System.out.println("Letter grade: A");
   }

   if (grade_input >= b) {
		  System.out.println("Letter grade: B");
   }

   		if (grade_input >= c) {
     System.out.println("Letter grade: C");
   		}

  if (grade_input >= d) {
			System.out.println("Letter grade: D");
  }

		
		if (grade_input < d) {
   System.out.println("Letter grade: F");
		}
	}
}