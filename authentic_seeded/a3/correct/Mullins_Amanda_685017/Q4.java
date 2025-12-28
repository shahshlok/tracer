import java.util.*;

public class Q4 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = in.nextInt();

	 int[] arr_numbers = new int[N];

    System.out.print("Enter elements: ");

    for (int i = 0; i < N; i++) {
      arr_numbers[i] = in.nextInt();
    }

    
    if (N > 0) {
    	 int last = arr_numbers[N - 1];

      for (int i = N - 1; i > 0; i--) {
      	 arr_numbers[i] = arr_numbers[i - 1];
      }

      arr_numbers[0] = last;
    }

    System.out.print("Shifted: ");
    for (int i = 0; i < N; i++) {
      System.out.print(arr_numbers[i]);
      if (i < N - 1) {
      	System.out.print(" ");
      }
    }
  }
}