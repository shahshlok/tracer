import java.util.*;

public class Q1 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
    int N = sc.nextInt();

		int[] arr = new int[N];

    System.out.print("Enter elements: ");
   for (int i = 0; i < N; i++) {
        arr[i] = sc.nextInt();
   }

      System.out.print("Enter target: ");
    int T = sc.nextInt();

int index_found = -1;

   for (int i = 0; i < N; i++) {
      if (arr[i] == T) {
          index_found = i;
          break;
      }
   }

   if (index_found == -1) {
        System.out.println("Found at index: -1");
   } else {
      System.out.println("Found at index: " + index_found);
   }

  }
}