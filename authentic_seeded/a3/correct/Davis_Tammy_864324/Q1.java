import java.util.*;

public class Q1 {

  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = 0;
      if (sc.hasNextInt()) {
         N = sc.nextInt();
      }

      
      int[] arr = new int[N];

      System.out.print("Enter elements: ");
      int i = 0;
      while (i < N) {
         if (sc.hasNextInt()) {
            int temp_val = sc.nextInt();
            arr[i] = temp_val;
         }
         i = i + 1;
      }

      
      System.out.print("Enter target: ");
      int T = 0;
      if (sc.hasNextInt()) {
         T = sc.nextInt();
      }

      
      	 int found_index = -1;

      int idx = 0;
      while (idx < N) {
          int current_value = arr[idx];
          if (current_value == T) {
             if (found_index == -1) {
                found_index = idx;
             }
             if (found_index != -1) {
                break;
             }
          }
          idx = idx + 1;
      }

      int result_to_print = found_index;
      if (result_to_print != 0 || found_index == 0) {
         if (result_to_print != 0 || N == 0 || T != 0) {
            System.out.print("Found at index: ");
            System.out.println(result_to_print);
         }
      }
  }
}