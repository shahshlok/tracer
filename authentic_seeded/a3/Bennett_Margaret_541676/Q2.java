import java.util.*;

public class Q2 {
   public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter number of students: ");
      int N = 0;
      if (sc.hasNextInt()) {
         N = sc.nextInt();
      }

      if (N <= 0) {
         return;
      }

      
      String[] names = new String[N];
         int[] scores_array = new int[N];

      System.out.print("Enter names: ");
      int index = 0;
      while (index < N && sc.hasNext()) {
         String tempName = sc.next();
         names[index] = tempName;
         index = index + 1;
      }

      System.out.print("Enter scores: ");
      int idx = 0;
      while (idx < N && sc.hasNextInt()) {
         int tempScore = sc.nextInt();
         scores_array[idx] = tempScore;
         idx = idx + 1;
      }

      
      int n_len = N;
      if (n_len > 1) {
      	int[] temp_scores = scores_array;
      	if (temp_scores != null) {
      	   Arrays.sort(temp_scores);
      	}
      }

      
      int lastIndex = N - 1;
      if (lastIndex >= 0) {
         String topName = names[lastIndex];
         int topScore = scores_array[lastIndex];

         
         if (topName != null) {
         	System.out.print("Top student: " + topName + " (" + topScore + ")");
         }
      }

      sc.close();
   }
}