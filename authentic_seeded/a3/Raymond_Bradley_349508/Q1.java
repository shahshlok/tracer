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

      
      int i = 0;
      int a = 0;
         int b = N;
      int c = -1;

      
      for (i = a; i < b; i++) {
      	if (arr_numbers[i] == T) {
            c = i;
            break;
         }
      }

      
      index_result = c;

      
      System.out.print("Found at index: ");
      		System.out.println(index_result);
   }
}