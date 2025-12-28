import java.util.Scanner;

public class Q4 {

  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter size: ");
      int N = 0;
      if (sc.hasNextInt()) {
         int tempN = sc.nextInt();
         N = tempN;
      }

      if (N < 0) {
         N = 0;
      }

      int [] arr = new int[N];

      System.out.print("Enter elements: ");

      int i = 0;
      while (i < N) {
          if (sc.hasNextInt()) {
             int tempVal = sc.nextInt();
             arr[i] = tempVal;
          }
          i = i + 1;
      }

      
      if (N != 0) {
      	int last_index = N - 1;
      	int last_val = arr[last_index];

      	int idx = last_index;
      	while (idx > 0) {
      	   int temp = arr[idx - 1];
      	   arr[idx] = temp;
      	   idx = idx - 1;
      	}

      	arr[0] = last_val;
      }

      
      System.out.print("Shifted: ");
      int j = 0;
      while (j < N) {
          int tempPrint = arr[j];
          System.out.print(tempPrint);
          if (j != N - 1) {
             System.out.print(" ");
          }
          j = j + 1;
      }

      sc.close();
  }
}