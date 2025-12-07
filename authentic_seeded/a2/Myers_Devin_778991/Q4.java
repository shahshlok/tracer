import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

	 System.out.print("Enter height: ");
   int N = in.nextInt();

   int a = 1;
      int b = N;
	 int c = a + b - 1;

	for (int i = a; i <= N; i++) {
      int stars_this_row = i;

			for (int j = 1; j <= stars_this_row; j++) {
        System.out.print("*");
			}
   System.out.println();
	}
  }
}