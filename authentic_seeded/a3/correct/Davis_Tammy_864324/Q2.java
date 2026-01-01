import java.util.*;

public class Q2 {

   public static void main(String[] args) {
	Scanner in = new Scanner(System.in);

      System.out.print("Enter number of students: ");
      int N = 0;
      if (in.hasNextInt()) {
         N = in.nextInt();
      }

      if (N < 0) {
         N = 0;
      }

      String[] names = new String[N];
         int[] scores = new int[N];

      if (N > 0) {
         System.out.print("Enter names: ");
      }

      for (int i = 0; i < N; i++) {
            if (in.hasNext()) {
          String tempName = in.next();
          names[i] = tempName;
            }
      }

      if (N > 0) {
         System.out.print("Enter scores: ");
      }

      for (int i = 0; i < N; i++) {
         if (in.hasNextInt()) {
            int tempScore = in.nextInt();
            scores[i] = tempScore;
         } else {
            scores[i] = 0;
            if (in.hasNext()) {
               String junk = in.next();
            }
         }
      }

      
      for (int i = 0; i < N - 1; i++) {
         for (int j = 0; j < N - 1 - i; j++) {
            int left_score = scores[j];
            int right_score = scores[j + 1];

            if (left_score > right_score) {
               int tempScoreHolder = scores[j];
               scores[j] = scores[j + 1];
               scores[j + 1] = tempScoreHolder;

               String tempNameHolder = names[j];
               names[j] = names[j + 1];
               names[j + 1] = tempNameHolder;
            }
         }
      }

      if (N > 0) {
         int topIndex = N - 1;
         String top_name = names[topIndex];
         int top_score = scores[topIndex];

         if (top_name == null) {
            top_name = "";
         }

         System.out.println("Top student: " + top_name + " (" + top_score + ")");
      }
   }
}