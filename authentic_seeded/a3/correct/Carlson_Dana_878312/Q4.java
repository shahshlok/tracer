import java.util.*;

public class Q4 {
  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = 0;
      if (sc.hasNextInt()) {
         N = sc.nextInt();
      }

      if (N < 0) {
         N = 0;
      }

      int[] arr = new int[N];

      System.out.print("Enter elements: ");
      for (int i = 0; i < N; i++) {
          if (sc.hasNextInt()) {
             int temp_val = sc.nextInt();
             arr[i] = temp_val;
          } else {
             arr[i] = 0;
          }
      }

      
      if (N > 1) {
         int last_index = N - 1;
         int last_val = arr[last_index];

         int i_holder = N - 1;
         while (i_holder > 0) {
            int prev_index = i_holder - 1;
            if (prev_index >= 0) {
               arr[i_holder] = arr[prev_index];
            }
            i_holder = i_holder - 1;
         }

         if (N != 0) {
            arr[0] = last_val;
         }
      }

      
    	   System.out.print("Shifted: ");
      for (int i = 0; i < N; i++) {
         int value_to_print = arr[i];
         System.out.print(value_to_print);
         if (i != N - 1) {
            System.out.print(" ");
         }
      }
  }
}