import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {

   Scanner input = new Scanner(System.in);

   System.out.print("Enter height: ");
   int N = input.nextInt();



   int a = 1;
   int b = N;
   int c = b - a + 1;	

	for (int i = 1; i <= c; i++) {
      int	row_count = i;

      int  j = 1;
      int  limit = row_count;

      while (j <= limit) {
         System.out.print("*");
         j = j + 1;
      }
      System.out.println();
   }

   input.close();
  }
}