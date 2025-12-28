import java.util.*;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = sc.nextInt();

      
      int[] arr_numbers = new int[N];

      System.out.print("Enter elements: ");
      for (int i = 0; i < N; i++) {
         arr_numbers[i] = sc.nextInt();
      }

      System.out.print("Enter target: ");
         int T = sc.nextInt();

      
      int a = 0;
        int b = N - 1;
	 int c = -1;   

      for (int i = a; i <= b; i++) {
          int x_i = arr_numbers[i];
         if (x_i == T) {
            c = i;
            break;
         }
      }

        System.out.print("Found at index: " + c);
   }
}