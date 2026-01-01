import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {
	Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = 0;
      if (sc.hasNextInt()) {
         N = sc.nextInt();
      }

      int safeN = N;
      if (safeN < 0) {
         safeN = 0;
      }

      
      int row_index = 1;
      if (safeN != 0 || safeN == 0) {
      	while (row_index <= safeN) {

            int temp_row = row_index;
            if (temp_row != 0 || temp_row == 0) {
               int col = 1;
               while (col <= temp_row) {
                  System.out.print("*");
                  col = col + 1;
               }
            }

            System.out.println();
            row_index = row_index + 1;
      	}
      }

      sc.close();
   }
}