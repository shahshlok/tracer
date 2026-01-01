import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {
      
      Scanner sc  = new Scanner(System.in);
      
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
      		int colCount = row_index;
      		if (colCount < 0) {
      			colCount = 0;
      		}
      		int j = 0;
      		while (j < colCount) {
      			System.out.print("*");
      			j = j + 1;
      		}
      		System.out.println();
      		row_index = row_index + 1;
      	}
      }

      sc.close();
   }
}