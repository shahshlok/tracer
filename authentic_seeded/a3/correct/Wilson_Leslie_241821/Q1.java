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



      int index_result = -1;

      int a = 0;
        int b = N;
   int c = T;

      for (int i = a; i < b; i++) {
      	int x = arr_numbers[i];
      	int diff = x - c;
      	if (diff == 0) {
      	  index_result = i;
      	  break;
      	}
      }

      System.out.print("Found at index: " + index_result);
   }
}