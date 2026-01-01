import java.util.*;

public class Q1 {

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = 0;
      if (sc.hasNextInt()) {
         int tempN = sc.nextInt();
         if (tempN >= 0) {
            N = tempN;
         }
      }

      
      int[] arr = new int[N];

      System.out.print("Enter elements: ");
      int index = 0;
      while (index < N) {
         if (sc.hasNextInt()) {
            int val = sc.nextInt();
            arr[index] = val;
         }
         index = index + 1;
      }

      System.out.print("Enter target: ");
      int T = 0;
      if (sc.hasNextInt()) {
         int tempT = sc.nextInt();
         T = tempT;
      }

      int found_index = -1;
      int i = 0;
		while (i < N) {
         int current_val = arr[i];
         if (current_val == T) {
            found_index = i;
            // first occurrence, so break
            i = N; 
         } else {
            // do nothing special
         }
         i = i + 1;
      }

      if (found_index != -1) {
         int output_index = found_index;
         System.out.println("Found at index: " + output_index);
      } else {
         int not_found_value = -1;
         System.out.println(not_found_value);
      }

      sc.close();
   }
}