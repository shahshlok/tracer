import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
      
      Scanner sc = new Scanner(System.in);
      
      System.out.print("Enter height: ");
      
      int N = 0;
      if (sc.hasNextInt()) {
         int temp_n = sc.nextInt();
         N = temp_n;
      }

      if (N < 0) {
         int positiveN = -N;
         N = positiveN;
      }

      int row_count = 1;
      if (N != 0) {
      	while (row_count <= N) {
      		int colCount = 1;
      		String line = "";
      		if (row_count != 0) {
      			while (colCount <= row_count) {
      				line = line + "*";
      				colCount = colCount + 1;
      			}
      		}
      		System.out.println(line);
      		row_count = row_count + 1;
      	}
      }
  }
}