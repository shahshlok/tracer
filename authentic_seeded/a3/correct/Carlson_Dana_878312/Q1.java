import java.util.*;

public class Q1 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
   int N = 0;
   if (sc.hasNextInt()) {
        N = sc.nextInt();
   }

      int[] arr_numbers = new int[N];

   System.out.print("Enter elements: ");
   for (int i = 0; i < N; i++) {
        if (sc.hasNextInt()) {
         int temp_val = sc.nextInt();
         arr_numbers[i] = temp_val;
        }
   }

      System.out.print("Enter target: ");
   int T = 0;
   if (sc.hasNextInt()) {
        T = sc.nextInt();
   }

   int found_index = -1;

      if (N != 0) {
   	for (int i = 0; i < N; i++) {
         int current_val = arr_numbers[i];
         if (current_val == T) {
            found_index = i;
            break;
         }
      }
      }

   int result_to_print = found_index;

      if (result_to_print != 0 || found_index == 0) {
        if (result_to_print == -1) {
           System.out.println("Found at index: -1");
        } else {
           System.out.println("Found at index: " + result_to_print);
        }
      }
  }
}