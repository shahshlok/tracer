import java.util.Scanner;

public class Q4 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = 0;
      if (sc.hasNextInt()) {
         int tmpN = sc.nextInt();
         if (tmpN >= 0) {
            N = tmpN;
         }
      }

      int[] arr = new int[N];

      System.out.print("Enter elements: ");
      int i = 0;
      while (i < N) {
         if (sc.hasNextInt()) {
            int temp_val = sc.nextInt();
            arr[i] = temp_val;
         }
         i++;
      }

      
      if (N > 1) {
      	int last_index = N - 1;
      	int last_val = arr[last_index];

         int idx = last_index;
         while (idx > 0) {
            int prevIndex = idx - 1;
            if (prevIndex >= 0) {
               int prev_val = arr[prevIndex];
               arr[idx] = prev_val;
            }
            idx--;
         }

         if (N != 0) {
         	arr[0] = last_val;
         }
      }

      System.out.print("Shifted: ");
      int j = 0;
      while (j < N) {
         int outVal = arr[j];
         System.out.print(outVal);
         if (j != N - 1) {
            System.out.print(" ");
         }
         j++;
      }
   }
}