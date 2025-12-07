import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      System.out.print("Enter height: ");
      
      int height = input.nextInt();

      
      int a = 1;
      int b = height;
      int c = b - a + 1;

      
      for (int row = 1; row < c; row++) {
      	int j_start = 1;
      	int j_end = row;
      	for (int col = j_start; col <= j_end; col++) {
          System.out.print("*");
      	}
      	System.out.println();
      }
   }
}