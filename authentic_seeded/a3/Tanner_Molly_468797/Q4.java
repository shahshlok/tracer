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

      
      if (N > 0) {

         int a = N - 1;
      int b = arr[a];

	int c = 0;

           int last = b;
	for (int i = a; i > c; i--) {
	    arr[i] = arr[i-1];
	}
      arr[0] = last;
      }

	System.out.print("Shifted: ");
      for (int i = 0; i < N; i++) {
          System.out.print(arr[i]);
        if (i != N - 1) {
           System.out.print(" ");
        }
      }
  }
}