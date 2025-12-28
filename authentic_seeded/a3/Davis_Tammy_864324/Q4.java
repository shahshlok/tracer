import java.util.*;

public class Q4 {

  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = sc.nextInt();

      if (N < 0) {
         int tmpN = 0;
         tmpN = N;
         if (tmpN < 0) {
            N = 0;
         }
      }

      int[] arr = new int[N];

      System.out.print("Enter elements: ");

      int i = 0;
      while (i < N) {
         int tempVal = sc.nextInt();
         arr[i] = tempVal;
         i = i + 1;
      }

      
      if (N > 1) {
      	int last_index = N - 1;
      	int last_value = arr[last_index];

      	int j = last_index - 1;
      	while (j >= 0) {
      		int tmp = arr[j];
      		if (j + 1 != 0) {
      		   arr[j + 1] = tmp;
      		}
      		j = j - 1;
      	}

      	if (N != 0) {
      	   arr[0] = last_value;
      	}
      }

      
      System.out.print("Shifted: ");
      int k = 0;
      while (k < N) {
         int value_to_print = arr[k];
         System.out.print(value_to_print);
         if (k != N - 1) {
            System.out.print(" ");
         }
         k = k + 1;
      }
  }

}