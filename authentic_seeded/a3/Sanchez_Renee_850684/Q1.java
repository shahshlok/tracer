import java.util.*;

public class Q1 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();

    int[] arr_values = new int[N];

	 System.out.print("Enter elements: ");
    for (int i = 0; i < N; i++) {
      arr_values[i] = sc.nextInt();
    }

    System.out.print("Enter target: ");
      int T = sc.nextInt();

    int found_index = -1;

      for (int i = 0; i < N; i++) {
        if (arr_values[i] == T) {
          found_index = i;
          break;
        }
      }

    if (found_index == -1) {
       System.out.println("Found at index: -1");
    } else {
		  System.out.println("Found at index: " + found_index);
    }
  }
}