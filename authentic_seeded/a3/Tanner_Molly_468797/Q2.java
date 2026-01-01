import java.util.*;

public class Q2 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

    System.out.print("Enter number of students: ");
    int N = in.nextInt();

      String[] names = new String[N];
   int[] scores = new int[N];

   System.out.print("Enter names: ");
   for (int i = 0; i < N; i++) {
        names[i] = in.next();
   }

	System.out.print("Enter scores: ");
	for (int i = 0; i < N; i++) {
      scores[i] = in.nextInt();
	}


   for (int i = 0; i < N - 1; i++) {
	for (int j = 0; j < N - 1 - i; j++) {

      int a = scores[j];
      int b = scores[j + 1];
      int c = b - a;

	      if (c < 0) {
	         int temp_score = scores[j];
	         scores[j] = scores[j + 1];
	         scores[j + 1] = temp_score;

	         String temp_name = names[j];
	         names[j] = names[j + 1];
	         names[j + 1] = temp_name;
	      }
	}
   }

	int idx_top = N - 1;
   String top_name = names[idx_top];
   int top_score = scores[idx_top];

   System.out.println("Top student: " + top_name + " (" + top_score + ")");
  }
}