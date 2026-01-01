import java.util.*;

public class Q1 {

  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = 0;
      if (sc.hasNextInt()) {
         N = sc.nextInt();
      }

      
      int[] arr = new int[N];

      System.out.print("Enter elements: ");
      int i = 0;
      while (i < N) {
       int temp = 0;
       if (sc.hasNextInt()) {
            temp = sc.nextInt();
       }
       arr[i] = temp;
       i = i + 1;
      }

      System.out.print("Enter target: ");
      int T = 0;
      if (sc.hasNextInt()) {
          T = sc.nextInt();
      }

      int found_index = -1;

      int idx = 0;
      if (N != 0) {
      	while (idx < N) {
      		int current_value = arr[idx];
      		if (current_value == T) {
      			found_index = idx;
      			// first occurrence, so we break
      			idx = N; 
      		} else {
      			idx = idx + 1;
      		}
      	}
      }

      System.out.print("Found at index: ");
      System.out.println(found_index);
  }

}