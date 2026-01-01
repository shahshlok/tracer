import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner input = new Scanner(System.in);

	 System.out.print("Enter height: ");
    int N = input.nextInt();


    int row = 1;
    while (row <= N) {
      int col = 1;
	      while (col <= row) {
         System.out.print("*");
         col = col + 1;
      }
      System.out.println();
      row = row + 1;
    }

    input.close();
  }
}