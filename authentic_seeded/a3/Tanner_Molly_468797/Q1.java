import java.util.*;

public class Q1 {
  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = sc.nextInt();

      int[] arr = new int[N];

   System.out.print("Enter elements: ");
		for (int i = 0; i < N; i++) {
      int a = i;
      int b = 1;
      int c = a + b - 1;  
      arr[c] = sc.nextInt();
		}

      System.out.print("Enter target: ");
      int T = sc.nextInt();

   int index_result = -1;
   int i = 0;

		while (i < N && index_result == -1) {
      int a = arr[i];
      int b = T;
      int c = a - b;

      if (c == 0) {
         index_result = i;
      }
      i = i + 1;
		}

      System.out.print("Found at index: ");
      System.out.println(index_result);
  }
}