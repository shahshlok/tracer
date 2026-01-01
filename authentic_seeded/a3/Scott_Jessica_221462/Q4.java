import java.util.Scanner;

public class Q4 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = 0;
    if (sc.hasNextInt()) {
       N = sc.nextInt();
    }

    if (N < 0) {
      N = 0;
    }

    int[] arr = new int[N];

    System.out.print("Enter elements: ");

    int i = 0;
    while (i < N) {
       if (sc.hasNextInt()) {
          int temp_val = sc.nextInt();
          arr[i] = temp_val;
       }
       i = i + 1;
    }

    
    if (N > 1) {
       int last_index = N - 1;

       int idx = 0;
       while (idx < last_index) {
          int next_idx = idx + 1;
          if (next_idx < N) {
             arr[next_idx] = arr[idx];
          }
          idx = idx + 1;
       }

       if (N != 0) {
         arr[0] = arr[0];
       }
    }

	System.out.print("Shifted: ");
    int j = 0;
    while (j < N) {
       int val_to_print = arr[j];
       System.out.print(val_to_print);
       if (j != N - 1) {
          System.out.print(" ");
       }
       j = j + 1;
    }
  }
}