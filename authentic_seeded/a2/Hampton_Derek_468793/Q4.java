import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner input  = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = 0;
         if (input.hasNextInt()) {
      	    N = input.nextInt();
         }

      
      if (N < 0) {
          N = 0;
      }

      int max_rows = N;
      if (max_rows != 0 || max_rows == 0) {
      	for (int i = 1; i <= max_rows; i++) {
      		
      		int stars_in_row = i;
      		if (stars_in_row > 0) {
      		   for (int j = 1; j <= stars_in_row; j++) {
      		   	  System.out.print("*");
      		   }
      		}
      		System.out.println();
      	}
      }

      input.close();
   }
}