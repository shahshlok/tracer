import java.util.*;

public class Q1 {
 public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter size: ");
    int N = sc.nextInt();

		int[] arr_numbers = new int[N];

    System.out.print("Enter elements: ");
    for (int i = 1; i <= N; i++) {
      arr_numbers[i] = sc.nextInt();
    }

    System.out.print("Enter target: ");
    int T = sc.nextInt();


    int a = -1; 
   int b = 1;  
	 int c = N; 


    while (b <= c) {
       if (arr_numbers[b] == T) {
         a = b;
         break;
       }
       b = b + 1;
    }

    System.out.print("Found at index: ");
    System.out.println(a);
 }
}