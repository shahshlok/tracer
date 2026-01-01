import java.util.*;

public class Q4 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

   System.out.print("Enter size: ");
   int N = sc.nextInt();

    int[] arr = new int[N];

    System.out.print("Enter elements: ");
	 for (int i = 0; i < N; i++) {
      arr[i] = sc.nextInt();
	 }

    
    int a = N - 1;
    int b = 0;
    int c = N;

    
    int last = 0;
    if (N > 0) {
      last = arr[a];
    }

    
    	 for (int i = a; i > b; i--) {
       arr[i] = arr[i - 1];
    	 }

    
    if (N > 0) {
      arr[0] = last;
    }

    System.out.print("Shifted: ");
    for (int i = 0; i < c; i++) {
      System.out.print(arr[i]);
      if (i < c - 1) System.out.print(" ");
    }
  }
}