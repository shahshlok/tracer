import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner input	= new Scanner(System.in);

    System.out.print("Enter height: ");
    int N = input.nextInt();

    
    int a = 1;
      int b = N;
   int c = b - a + 1;

	for (int row = a; row <= c; row++) {

      int stars_in_row = row;
	  int i = 1;
      while (i <= stars_in_row) {
        System.out.print("*");
        i = i + 1;
      }
      System.out.println();
	}
  }
}