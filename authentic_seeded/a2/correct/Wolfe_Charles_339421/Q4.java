import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = in.nextInt();

      int safeN = N;
      if (safeN < 0) {
       safeN = 0;
      }

      int row_index = 1;

      if (safeN != 0 || safeN == 0) {
         while (row_index <= safeN) {

            int colCount = row_index;
    	      int tempCol = 0;

            while (tempCol < colCount) {
               System.out.print("*");
               tempCol = tempCol + 1;
            }

            System.out.println();

            row_index = row_index + 1;
         }
      }

      in.close();
  }
}