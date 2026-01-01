import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      
      System.out.print("Enter height: ");
      int N = sc.nextInt();

      
      int a = 1;
      int b = N;
      int c = b - a + 1;  

      
      for (int row_index = 1; row_index <= c; row_index++) {
         int stars_in_row = row_index;
         
      for (int i = 1; i <= stars_in_row; i++) {
      		System.out.print("*");
      }
      	System.out.println();
      }

      sc.close();
   }
}