import java.util.*;

public class Q4 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = in.nextInt();

      int[] arr = new int[N];

    System.out.print("Enter elements: ");
	 for (int i = 0; i < N; i++) {
      arr[i] = in.nextInt();
	 }

    
    if (N > 0) {
        int a = arr[N - 1];  
        int b = N - 2;       
        int c = 0;           
        

	    for (int i = b; i >= c; i--) {
      	arr[i + 1] = arr[i];
	    }

        arr[0] = a;
    }

    System.out.print("Shifted: ");
      for (int i = 0; i < N; i++) {
        System.out.print(arr[i]);
        if (i < N - 1) System.out.print(" ");
      }
  }
}