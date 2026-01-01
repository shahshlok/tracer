import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {

   Scanner scanner   = new Scanner(System.in);

	 System.out.print("Enter height: ");
    int N = scanner.nextInt();

    int current_row = 1;

    while (current_row <= N) {

      int starCount	= 1;
      while (starCount <= current_row) {
        System.out.print("*");
        starCount++;
      }

      System.out.println();
      current_row++;
    }

    scanner.close();
  }
}