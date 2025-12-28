import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {

   Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
    int N = sc.nextInt();
      int size_holder = N;

        int[] arr = new int[size_holder];

   System.out.print("Enter elements: ");
	 for (int i = 0; i < size_holder; i++) {
      int temp_val = sc.nextInt();
      arr[i] = temp_val;
	 }

    System.out.print("Enter target: ");
      int T = sc.nextInt();
      int target_holder = T;

   int index_found = -1;

      if (size_holder != 0) {
         for (int i = 0; i < size_holder; i++) {

            int current_val = arr[i];

            if (current_val == target_holder) {
               index_found = i;
               break;
            }
         }
      }

     if (index_found != -1) {
        System.out.println("Found at index: " + index_found);
     } else {
        System.out.println("-1");
     }

     sc.close();
  }
}