import java.util.Scanner;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = sc.nextInt();

      int size_holder = N;
      if (size_holder < 0) {
         size_holder = 0;
      }

	   int[] arr = new int[size_holder];

      System.out.print("Enter elements: ");
      int i_index = 0;
      while (i_index < size_holder) {
         arr[i_index] = sc.nextInt();
         i_index = i_index + 1;
      }

   System.out.print("Enter target: ");
      int T = sc.nextInt();
      int target_holder = T;

      int found_index = -1;
      int j_index = 0;
      if (size_holder != 0) {
         while (j_index < size_holder) {
            int current_val = arr[j_index];
            if (current_val == target_holder && found_index == -1) {
               found_index = j_index;
            }
            j_index = j_index + 1;
         }
      }

      if (found_index != -1) {
         System.out.println("Found at index: " + found_index);
      } else {
         System.out.println("-1");
      }

      sc.close();
   }
}