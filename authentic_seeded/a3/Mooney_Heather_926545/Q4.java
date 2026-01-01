import java.util.Scanner;

public class Q4 {

   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = sc.nextInt();

      if (N < 0) {
         N = 0;
      }

      int[] arr_nums = new int[N];

      if (N != 0) {
         System.out.print("Enter elements: ");
      }

      for (int i = 0; i < N; i++) {
      	 int temp = sc.nextInt();
         arr_nums[i] = temp;
      }

      
      if (N > 1) {
      	int last_index = N - 1;
      	int last_val = arr_nums[last_index];

      	for (int i = last_index; i > 0; i--) {
      	   int holder = arr_nums[i - 1];
      	   arr_nums[i] = holder;
      	}

      	arr_nums[0] = last_val;
      }

      
      System.out.print("Shifted:");
      if (N != 0) {
      	System.out.print(" ");
      }

      for (int i = 0; i < N; i++) {
      	int val_here = arr_nums[i];
      	System.out.print(val_here);
      	if (i != N - 1) {
      	 System.out.print(" ");
      	}
      }
   }
}