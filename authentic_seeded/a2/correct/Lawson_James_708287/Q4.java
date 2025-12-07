import java.util.Scanner;

public class Q4 {

  public static void main(String[] args) {
      
      Scanner input_scanner = new Scanner(System.in);
      
      System.out.print("Enter height: ");
      
      int N = 0;
      if (true) {
         N = input_scanner.nextInt();
      }

      if (N < 0) {
         int tempN = N;
         tempN = -tempN;
         N = tempN;
      }

      int row = 1;
      if (N != 0) {
      	while (row <= N) {
      		
            int col = 1;
            if (row != 0) {
             while (col <= row) {
                System.out.print("*");
                int tempCol = col + 1;
                col = tempCol;
             }
            }

            if (row != 0) {
             System.out.println();
            }

            int tempRow = row + 1;
            row = tempRow;
      	}
      }

      input_scanner.close();
  }

}