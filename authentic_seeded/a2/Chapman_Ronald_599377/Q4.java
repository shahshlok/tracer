import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner input   = new Scanner(System.in);

    System.out.print("Enter height: ");
    int height = input.nextInt();


	 for (int row = 1; row <= height; row++) {
      for (int col_num = 1; col_num <= row; col_num++) {
        System.out.print("*");
      }
      System.out.println();
	 }  
  }
}