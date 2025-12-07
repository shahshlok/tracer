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

      
      int i = 1;
   if (height_holder >= 0) {
   	while (i <= height_holder) {

         int stars_in_row = i;
         
         int j = 0;
         if (stars_in_row >= 0) {
            while (j < stars_in_row) {
               System.out.print("*");
               j = j + 1;
            }
         }

         System.out.println();
         i = i + 1;
      }
   }

      sc.close();
   }
}