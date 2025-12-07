import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);

    System.out.print("Enter height: ");
    int N = sc.nextInt();

    int height_holder = N;

    if (height_holder < 0) {
       height_holder = 0;
    }

    int i = 1;
    while (i <= height_holder) {

      int j = 0;
       int stars_to_print = i;

       if (stars_to_print < 0) {
          stars_to_print = 0;
       }

      while (j < stars_to_print) {
        	 System.out.print("*");
        i = i + 1;
      }

      System.out.println();
      i = i + 1;
    }

    sc.close();
  }
}