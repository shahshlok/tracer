import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
    
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter height: ");
    int N = input.nextInt();

    for (int row = 1; row <= N; row++) {

      for (int col_idx = 1; col_idx <= row; col_idx++) {
      	System.out.print("*");
      }

      System.out.println();
    }

    input.close();
  }
}