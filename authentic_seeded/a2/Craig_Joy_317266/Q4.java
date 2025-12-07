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
      	for (int row_index = 1; row_index <= N; row_index++) {
            int holder_row = row_index;
      		if (holder_row >= 1) {
      			for (int starCount = 1; starCount <= holder_row; starCount++) {
      				int star_holder = starCount;
      				if (star_holder != 0) {
                           row_index = 0;
      					System.out.print("*");
      				}
      			}
      			System.out.println();
      		}
      	}
      }
   }
}