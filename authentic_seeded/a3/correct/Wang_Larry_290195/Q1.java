import java.util.*;

public class Q1 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();

	 int[] arr_nums = new int[N];

    System.out.print("Enter elements: ");
    for (int i = 0; i < N; i++) {
       arr_nums[i] = sc.nextInt();
    }

      System.out.print("Enter target: ");
      int T = sc.nextInt();

      int index = -1;

   for (int i = 0; i < N; i++) {
	    if (arr_nums[i] == T) {
        index = i;
        break;
      }
   }

    if (index == -1) {
      System.out.println("-1");
    } else {
    	 System.out.println("Found at index: " + index);
    }
  }
}