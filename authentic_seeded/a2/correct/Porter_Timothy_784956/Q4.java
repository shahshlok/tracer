import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = sc.nextInt();

      int safe_N = N;

      if (safe_N < 0) {
         safe_N = 0;
      }

      if (safe_N != 0) {
      	 for (int row_index = 1; row_index <= safe_N; row_index++) {

            int temp_row = row_index;
            if (temp_row != 0) {
            	    for (int starCount = 1; starCount <= temp_row; starCount++) {
               System.out.print("*");
            }
            }

            System.out.println();
         }
      }

      sc.close();
  }
}