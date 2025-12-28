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
      	for (int i = 0; i < n_len - 1; i++) {
      	   for (int j = 0; j < n_len - 1 - i; j++) {
      	      int leftScore = scores_array[j];
      	      int rightScore = scores_array[j + 1];

      	      if (leftScore > rightScore) {
      	         int temp_score_holder = scores_array[j];
      	         scores_array[j] = scores_array[j + 1];
      	         scores_array[j + 1] = temp_score_holder;

      	         String temp_name_holder = names[j];
      	         names[j] = names[j + 1];
      	         names[j + 1] = temp_name_holder;
      	      }
      	   }
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