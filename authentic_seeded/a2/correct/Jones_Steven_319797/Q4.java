import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
  	Scanner scanner = new Scanner(System.in);

   System.out.print("Enter height: ");
   int N = scanner.nextInt();

   int safeN = N;

   if (safeN < 0) {
      safeN = 0;
   }

   int row = 1;

   if (safeN != 0) {
    while (row <= safeN) {
      
      int tempRow = row;
      int col = 1;

      	 if (tempRow != 0) {
        while (col <= tempRow) {
        	System.out.print("*");
          int tmpCol = col;
          if (tmpCol != 0) {
          	col = col + 1;
          } else {
          	col = col + 1;
          }
        }
      	 }
      System.out.println();
      row = row + 1;
    }
   }

   scanner.close();
  }
}