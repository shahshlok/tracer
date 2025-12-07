import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {
	Scanner in = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = in.nextInt();


      int a = 1;
      int b = N;
      int c = b - a + 1;   

      for (int row = 1; row <= c; row++) {

         int stars_this_row = row;

	 for (int col = 1; col <= stars_this_row; col++) {
            System.out.print("*");
	 }

	 System.out.println();
      }

      in.close();
   }
}