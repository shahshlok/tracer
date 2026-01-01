import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = sc.nextInt();

      
      int a = 1;
      int b = N;
      int c = b - a + 1;  

      for (int i = a; i <= c; i++) {
      	int stars_in_row = i;

         for (int j = 1; j <= stars_in_row; j++) {
            System.out.print("*");
         }
         
         System.out.println();
      }

      sc.close();
   }
}