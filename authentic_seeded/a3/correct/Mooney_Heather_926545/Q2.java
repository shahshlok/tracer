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
         // no students, nothing meaningful to do, but avoid crash
         return;
      }

      String[] names = new String[N];
         int[] scores = new int[N];

      System.out.print("Enter names: ");
      for (int i = 0; i < N; i++) {
         if (sc.hasNext()) {
            String tempName = sc.next();
            names[i] = tempName;
         } else {
            names[i] = "";
         }
      }

      System.out.print("Enter scores: ");
      for (int j = 0; j < N; j++) {
         if (sc.hasNextInt()) {
            int tempScore = sc.nextInt();
            scores[j] = tempScore;
         } else {
            scores[j] = 0;
         }
      }


      // sort by scores ascending (parallel arrays)
      for (int i = 0; i < N - 1; i++) {
         for (int j = 0; j < N - 1 - i; j++) {
            int left = scores[j];
            int right = scores[j + 1];

            if (left > right) {
               int temp_score_holder = scores[j];
               scores[j] = scores[j + 1];
               scores[j + 1] = temp_score_holder;

               String tmp_name_holder = names[j];
               names[j] = names[j + 1];
               names[j + 1] = tmp_name_holder;
            }
         }
      }

      int top_index = N - 1;
      if (top_index < 0) {
         top_index = 0;
      }
      String top_name = names[top_index];
      int top_score = scores[top_index];

      if (top_name == null) {
         top_name = "";
      }

      System.out.println("Top student: " + top_name + " (" + top_score + ")");
   }
}