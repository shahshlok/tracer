import java.util.*;

public class Q4 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);
      
      System.out.print("Enter size: ");
      int N = sc.nextInt();
      
      int[] arr = new int[N];

         System.out.print("Enter elements: ");
      for (int i = 1; i <= N; i++) {
      	arr[i - 1] = sc.nextInt();
      }


      if (N > 0) {
      	int a = N;
      	int b = 1;
      	int c = arr[a - 1];

      	for (int i = a; i >= b; i--) {
      	  arr[i] = arr[i - 1];
      	}
      	arr[b - 1] = c;
      }

      
      System.out.print("Shifted: ");
      for (int i = 1; i <= N; i++) {
      	System.out.print(arr[i - 1]);
      	if (i != N) {
      	  System.out.print(" ");
      	}
      }
   }
}