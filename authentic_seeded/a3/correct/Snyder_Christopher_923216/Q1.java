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

   int a = 0;
      int b = N - 1;
  	 int index_result = -1;

   for (int i = a; i <= b; i++) {
        int c = arr[i] - T;
        if (c == 0) {
           index_result = i;
           break;
        }
   }

      if (index_result != -1) {
         System.out.println("Found at index: " + index_result);
      } else {
         System.out.println(-1);
      }
  }
}