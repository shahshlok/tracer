import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {

      Scanner sc = new Scanner(System.in);
    System.out.print("Enter height: ");
		int N = sc.nextInt();


      int a = 1;
   int b = N;
		int c = a + b - 1;  

      for (int row = a; row <= b; row++) {

         int stars_in_row = row;
      for (int i = 1; i <= stars_in_row; row++) {
				System.out.print("*");
      }
         System.out.println();
      }

      sc.close();
   }
}