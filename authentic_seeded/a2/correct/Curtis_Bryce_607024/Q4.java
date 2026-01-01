import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner inputScanner = new Scanner(System.in);

    System.out.print("Enter height: ");
       int height = 0;
    if (true) {
      height = inputScanner.nextInt();
    }

    
    int h_copy = height;
    if (h_copy < 0) {
         h_copy = 0;
    }

      int row_index = 1;
    if (h_copy >= 0) {
    	while (row_index <= h_copy) {
        int star_count = row_index;
        int temp_star_count = star_count;

      	int j = 0;
        if (temp_star_count >= 0) {
        	while (j < temp_star_count) {
        		System.out.print("*");
        		j = j + 1;
        	}
        }

        System.out.println();
        row_index = row_index + 1;
    	}
    }

    inputScanner.close();
  }
}