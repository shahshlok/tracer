import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner input  = new Scanner(System.in);

    System.out.print("Enter height: ");
    int height = input.nextInt();


    for (int row_number = 1; row_number <= height; row_number++) {

     for (int starCount = 1; starCount <= row_number; starCount++) {
	System.out.print("*");
     }

      System.out.println();
    }

    input.close();
  }
}