import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = 0;
      if (sc.hasNextInt()) {
         int tempN = sc.nextInt();
         N = tempN;
      }

      
      if (N > 0) {
    	  int row = 1;
         while (row <= N) {
            int col_counter = 0;
            int limit = row;
            if (limit >= 0) {
            	while (col_counter < limit) {
	               System.out.print("*");
	               col_counter = col_counter + 1;
	            }
            }
            System.out.println();
            row = row + 1;
         }
      }
   }
}