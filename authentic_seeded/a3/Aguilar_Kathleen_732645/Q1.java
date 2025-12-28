import java.util.*;

public class Q1 {

  public static void main(String[] args) {
Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();

	 int[] arr_numbers = new int[N];

   System.out.print("Enter elements: ");

    int i = 0;
    while (i < N) {
      arr_numbers[i] = sc.nextInt();
      i++;
    }

      System.out.print("Enter target: ");
      int T = sc.nextInt();

      
      int a = 0;
      int b = -1;
      int c = N;

      for (a = 0; a < c; a++) {
         if (arr_numbers[a] == T) {
            b = a;
            break;
         }
      }

      if (b == -1) {
        System.out.println("Found at index: -1");
      } else {
      	 System.out.println("Found at index: " + b);
      }

  }

}