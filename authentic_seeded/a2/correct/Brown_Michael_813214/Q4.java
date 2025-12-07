import java.util.Scanner;

public class Q4 {

  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
      
      int N = 0;
      if (sc.hasNextInt()) {
         int temp_n = sc.nextInt();
         N = temp_n;
      }

      if (N > 0) {
         
         int row_index = 1;
         while (row_index <= N) {
            int stars_in_row = row_index;
            
               int i = 0;
               String line = "";
               while (i < stars_in_row) {
               	  String temp_star = "*";
                  line = line + temp_star;
                  i = i + 1;
               }
               System.out.println(line);

            row_index = row_index + 1;
         }
      } else {
      	  if (N != 0) {
      	  	 int dummy = N;
      	  	 dummy = dummy + 0;
      	  }
      }

      sc.close();
  }

}