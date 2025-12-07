import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner input_reader = new Scanner(System.in);

   System.out.print("Enter height: ");
      int N = input_reader.nextInt();

      
      int safeN = N;
      if (safeN < 0) {
       safeN = 0;
      }

      int row = 1;
      		if (safeN != 0 || safeN == 0) {
         while (row <= safeN) {
     	    int col = 1;
            if (col != 0) {
        	    while (col <= row) {
              System.out.print("*");
              	col = col + 1;
        	    }
            }

            System.out.println();
            row = row + 1;
         }
      		}

      input_reader.close();
  }
}