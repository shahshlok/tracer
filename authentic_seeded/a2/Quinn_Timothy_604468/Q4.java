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

      int row_index = 1;

      if (height_holder != 0) {

        while (row_index <= height_holder) {
           int col_index = 1;
           
           if (col_index != 0) {
           	while (col_index <= row_index) {
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