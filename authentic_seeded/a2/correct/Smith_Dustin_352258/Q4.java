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
       N = 0;
    }

    int rowCount = 1;

    if (N != 0) {
      while (rowCount <= N) {
        
        int col_count = 1;
        if (rowCount != 0) {
           while (col_count <= rowCount) {
             String tempStar = "*";
             System.out.print(tempStar);
             col_count = col_count + 1;
           }
        }

        System.out.println();
        rowCount = rowCount + 1;
      }
    }

    input_scanner.close();
  }
}