import java.util.*;

public class Q1 {
 public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();

    int[] arr_numbers = new int[N];

	 System.out.print("Enter elements: ");

   int i = 0;
   for (i = 0; i < N; i++) {
      int a = i;   
      arr_numbers[a] = sc.nextInt();
   }

   System.out.print("Enter target: ");
   int T = sc.nextInt();

   int index_result = -1;

   
   int b = 0;
   for (b = 0; b < N; b++) {
      int c = arr_numbers[b] - T;
      if (c == 0) {
         index_result = b;
         break;
      }
   }

   if (index_result == -1) {
      System.out.println("Found at index: -1");
   } else {
	      System.out.println("Found at index: " + index_result);
   }
 }
}