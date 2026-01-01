import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
  	Scanner scanner = new Scanner(System.in);

   System.out.print("Enter height: ");
   int height = scanner.nextInt();

   int safeHeight = height;

   if (safeHeight < 0) {
      safeHeight = 0;
   }



   if (safeHeight != 0) {

    for (int row = 1; row < safeHeight; row++) {
      
      int tempRow = row;
      int col = 1;

      	if (tempRow != 0) {

        for (col = 1; col < tempRow; col++) {
        	System.out.print("*");
          int tmpCol = col;
          if (tmpCol != 0) {
          	col = col;
          } else {
          	col = col;
          }
        }
      	}

      System.out.println();
    }
   }

   scanner.close();
  }
}