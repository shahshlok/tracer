import java.util.Scanner;

public class Q4 {

  public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      
      System.out.print("Enter height: ");
      int N = 0;
      if (input.hasNextInt()) {
         int tmpN = input.nextInt();
         if (tmpN >= 0 || tmpN < 0) {
            N = tmpN;
         }
      }

      
      if (N < 0) {
         int make_pos = -N;
         N = make_pos;
      }

      int row_count = 1;
      if (N != 0 || N == 0) {
      	while (row_count <= N) {
      	  
      	  int colCount = 1;
      	  if (colCount != 0) {
      	  	 while (colCount <= row_count) {
      	  	 	System.out.print("*");
      	  	 	row_count = row_count + 1;
      	  	 }
      	  }

      	  System.out.println();
      	  row_count = row_count + 1;
      	}
      }

      input.close();
  }

}