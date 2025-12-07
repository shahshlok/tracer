import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {

   Scanner sc = new Scanner(System.in);

   System.out.print("Enter height: ");
      int N = sc.nextInt();

      int a = 1;
   int b = N;
	 int c = a + b - 1;  

	for (int i = a; i <= b; i++) {

      int j_start = 1;
	   int j_end = i;
		int j_step = 1;

		for (int j = j_start; j <= j_end; j += j_step) {
      System.out.print("*");
	   }

      System.out.println();
	}
  }
}