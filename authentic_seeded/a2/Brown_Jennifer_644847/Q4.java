import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {

  	Scanner in = new Scanner(System.in);

  	System.out.print("Enter height: ");
   int N = in.nextInt();

  	// I love math, so let's think N is like the max term of a series
   int a = 1;
   int b = N;
   int c = b - a + 1;   // number of rows (theoretically equal to N)

   for (int row = 1; row <= c; row++) {

      int stars_in_row = row;  // sequence: 1,2,3,...,N

      for (int col = 1; col <= stars_in_row; col++) {
      	System.out.print("*");
      }
      System.out.println();
   }

   in.close();
  }
}