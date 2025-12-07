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
      	for (int i=1; i<=N; i++) {
            
            int stars_in_row = i;
            if (stars_in_row != 0) {
            	for (int j = 0; j < stars_in_row; j++) {
            	    System.out.print("*");
            	}
            }
            System.out.println();
      	}
      }

      sc.close();
   }
}