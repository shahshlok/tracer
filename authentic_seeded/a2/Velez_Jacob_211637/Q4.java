import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = 0;
       if (input.hasNextInt()) {
          int temp_n = input.nextInt();
          N = temp_n;
       }



      if (N > 0) {
      	for (int i_row = 1; i_row <= N; i_row++) {

            int limit = i_row;
            if (limit >= 1) {
	         for (int j_col = 1; j_col <= limit; j_col++) {
	            System.out.print("*");
	         }
            }

	         System.out.println();
	      }
      }

      input.close();
   }
}