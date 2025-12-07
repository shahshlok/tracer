import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {

      Scanner input	= new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = input.nextInt();

      
      int a = 1;
      int b = N;
      int c = b - a + 1;  

      for (int i = a; i <= c; i++) {

         int row_count	= i;

         int j = 1;
         while (j <= row_count) {
      System.out.print("*");
            j = j + 1;
         }
         System.out.println();
      }

      input.close();
   }
}