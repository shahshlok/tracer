import java.util.Scanner;

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
      int idx = 0;
      while (idx < N) {
         if (sc.hasNextInt()) {
            int val = sc.nextInt();
            arr[idx] = val;
            idx = idx + 1;
         } else {
            sc.next();
         }
      }


      if (N > 1) {
      	int last_element = arr[N - 1];

         int i = N - 1;
         while (i > 0) {
            int temp = arr[i - 1];
            arr[i] = temp;
            i = i - 1;
         }

         arr[0] = last_element;
      }

      
      System.out.print("Shifted: ");
      int j = 0;
      while (j < N) {
         int hold = arr[j];
         System.out.print(hold);
         if (j != N - 1) {
            System.out.print(" ");
         }
         j = j + 1;
      }
   }
}