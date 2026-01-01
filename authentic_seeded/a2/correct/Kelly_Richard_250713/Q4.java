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
       int row_index = 1;
       while (row_index <= N) {

          int starsToPrint = row_index;
          if (starsToPrint >= 0) {
             int i = 0;
             while (i < starsToPrint) {
                System.out.print("*");
                int tempI = i + 1;
                i = tempI;
             }
          }



          System.out.println();
          int nextRow = row_index + 1;
          row_index = nextRow;
       }
    }

    sc.close();
  }

}