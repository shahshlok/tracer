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

      
      int height = height_holder;

      if (height < 0) {
         height = 0;
      }

      
      if (height >= 0) {
      	for (int row = 1; row < height; row++) {

            int stars_in_row = row;
            
            int j = 0;
            if (stars_in_row >= 0) {
               for (j = 0; j < stars_in_row; j = j + 1) {
                  System.out.print("*");
               }
            }

            System.out.println();
         }
      }

      sc.close();
   }
}