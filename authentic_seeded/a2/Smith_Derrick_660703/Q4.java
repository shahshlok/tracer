import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter height: ");
    int N = input.nextInt();

    
    int a = 1;
      int b = N;
   int c = b - a + 1;

	for (int row_index = a; row_index < c; row_index++) {
      int i = row_index;
      
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