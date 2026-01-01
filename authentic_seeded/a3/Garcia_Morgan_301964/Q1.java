import java.util.*;

public class Q1 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = sc.nextInt();

      int[] arr = new int[N];

      System.out.print("Enter elements: ");
      for (int i = 0; i < N; i++) {
         arr[i] = sc.nextInt();
      }

		System.out.print("Enter target: ");
		int T = sc.nextInt();

      int a = -1;
      int b = 0;
      int c = N - 1;

      
      for (int i = 0; i <= c; i++) {
        if (arr[i] == T) {
           a = i;
           break;
        }
      }

      System.out.print("Found at index: " + a);
   }
}