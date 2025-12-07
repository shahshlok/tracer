import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
         int N = sc.nextInt();


      int height_holder = N;
      if (height_holder < 0) {
      	height_holder = 0;
      }

      if (height_holder != 0 || height_holder == 0) {
      	int row_index = 1;

      	while (row_index <= height_holder) {

      		int columns_in_row = row_index;
      		int col_index = 1;

      		if (columns_in_row != 0 || columns_in_row == 0) {
      			while (col_index <= columns_in_row) {
      				System.out.print("*");
      				col_index = col_index + 1;
      			}
      		}

      		System.out.println();
      		row_index = row_index + 1;
      	}
      }

      sc.close();
   }
}