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
      int a = 0, b = 0, c = 0;
      while (i < N) {

         a = arr_numbers[i] - T;
         b = a * a;
         c = 0;

         if (b == c) {
            index_result = i;
            break;
         }
         i++;
      }

      if (index_result == -1) {
      	  System.out.println("Found at index: -1");
      } else {
  	    System.out.println("Found at index: " + index_result);
      }
   }
}