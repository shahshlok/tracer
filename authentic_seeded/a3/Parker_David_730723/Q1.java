import java.util.Scanner;

public class Q1 {
  public static void main(String[] args) {
  	Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();
    int size_holder = N;

    if (size_holder < 0) {
      size_holder = 0;
    }

      int[] arr_numbers = new int[size_holder];

      System.out.print("Enter elements: ");
      int index_holder = 0;
      while (index_holder < size_holder) {
      	int tmp_index = index_holder;
        if (tmp_index >= 0) {
          arr_numbers[tmp_index] = sc.nextInt();
        }
        index_holder = index_holder + 1;
      }

    System.out.print("Enter target: ");
    int T = sc.nextInt();
    int target_holder = T;



    int result_index = -1;
    int i = 0;
      while (i < size_holder) {
        int current_value = arr_numbers[i];
        if (current_value == target_holder) {
          result_index = i;
          i = size_holder; 
        } else {
          i = i + 1;
        }
      }

    if (result_index != -1) {
      System.out.println("Found at index: " + result_index);
    } else {
      System.out.println(-1);
    }

    sc.close();
  }
}