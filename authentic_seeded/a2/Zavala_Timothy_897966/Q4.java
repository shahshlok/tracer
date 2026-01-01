import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
  	Scanner input = new Scanner(System.in);

    System.out.print("Enter height: ");
    int height_n = input.nextInt();


    for (int row = 1; row <= height_n; row++) {
      for (int col = 1; col <= row; col++) {
      	System.out.print("*");
      }
      System.out.println();
    }
  }
}