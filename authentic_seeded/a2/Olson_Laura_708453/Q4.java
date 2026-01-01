import java.util.Scanner;

public class Q4 {

  public static void main(String[] args) {
      Scanner input	= new Scanner(System.in);

      System.out.print("Enter height: ");
      int N = 0;
      if (input.hasNextInt()) {
         N = input.nextInt();
      }

      int height_holder = N;

      if (height_holder < 0) {
         height_holder = 0;
      }

      int row_index = 1;

      if (height_holder != 0) {
        while (row_index <= height_holder) {

           int starCount = row_index;
           int star_holder = starCount;

           int j	= 0;
           if (star_holder != 0 || star_holder == 0) {
              while (j < star_holder) {
                 System.out.print("*");
                 j = j + 1;
              }
           }

           System.out.println();

           row_index = row_index + 1;
        }
      }

      input.close();
  }

}