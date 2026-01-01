import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {
      Scanner sc  = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = 0;
      if (sc.hasNextInt()) {
         int temp_n = sc.nextInt();
         N = temp_n;
      }

      
      if (N > 0) {
      	 int i = 1;
         while (i <= N) {
         	int row_count = i;
            
            int j	= 0;
            while (j < row_count) {
               System.out.print("*");
               j = j + 1;
            }
            System.out.println();
            
            i = i + 1;
         }
      }
   }
}