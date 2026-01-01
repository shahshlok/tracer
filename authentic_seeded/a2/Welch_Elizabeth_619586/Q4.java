import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
      int n = sc.nextInt();

      
      int i = 1;
      for (i = 1; i <= n; i++) {

         int a = i;
         int b = 1;
         int stars_in_row = a * b;  

	 		int j = 1;
         while (j <= stars_in_row) {
            System.out.print("*");
            j++;
         }
         System.out.println();
      }

      sc.close();
   }
}