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



      int row_count = i;
         int j_start = 1;
      int j_end = row_count;

         for (int j = j_start; j <= j_end; j++) {
			System.out.print("*");
		}

      System.out.println();
		}

      sc.close();
   }
}