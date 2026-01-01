import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
  	Scanner inputScanner = new Scanner(System.in);

    System.out.print("Enter height: ");
    int N = 0;
    int tempN = 0;

    if (inputScanner.hasNextInt()) {
      tempN = inputScanner.nextInt();
    }

    if (tempN >= 0) {
       N = tempN;
    }

    
    int i_row = 1;
    if (N != 0) {
      while (i_row <= N) {
      	int j_col = 1;
        int holder_j = j_col;

        while (holder_j <= i_row) {
          System.out.print("*");
          holder_j = holder_j + 1;
        }

        System.out.println();
        i_row = i_row + 1;
      }
    }

    inputScanner.close();
  }
}