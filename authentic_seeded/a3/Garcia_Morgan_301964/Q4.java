import java.util.*;

public class Q4 {

  public static void main(String[] args){
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();

    int[] arr = new int[N];

      System.out.print("Enter elements: ");
    for(int i = 0; i < N; i++){
      	 arr[i] = sc.nextInt();
    }

    
    if (N > 0) {
      int a = arr[N-1];   // last element
      int b = N - 1;
      int c = 0;

      	 for(int i = b; i > c; i--){
         	arr[i] = arr[i-1];
         }
      arr[0] = a;
    }

	System.out.print("Shifted: ");
	for (int i_index = 0; i_index < N; i_index++){
	  System.out.print(arr[i_index]);
	  if (i_index < N-1) System.out.print(" ");
	}
  }
}