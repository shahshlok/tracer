import java.util.*;

public class Q2 {

   public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

      System.out.print("Enter number of students: ");
      int N = 0;
      if (sc.hasNextInt()) {
         N = sc.nextInt();
      }

      if (N < 0) {
         N = 0;
      }

      String[] names = new String[N];
		int[] scores = new int[N];

      System.out.print("Enter names: ");
      int index = 0;
      while (index < N) {
         if (sc.hasNext()) {
            String tempName = sc.next();
            names[index] = tempName;
         } else {
            names[index] = "";
         }
         index++;
      }

      System.out.print("Enter scores: ");
      int i = 0;
      while (i < N) {
         if (sc.hasNextInt()) {
            int tempScore = sc.nextInt();
            scores[i] = tempScore;
         } else {
            scores[i] = 0;
            sc.next();
         }
         i++;
      }


      for (int a = 0; a < N - 1; a++) {
      	for (int b = 0; b < N - 1 - a; b++) {
            int leftScore = scores[b];
            int rightScore = scores[b + 1];
            if (leftScore > rightScore) {
               int temp_score_holder = scores[b];
               scores[b] = scores[b + 1];
               scores[b + 1] = temp_score_holder;

               String temp_name_holder = names[b];
               names[b] = names[b + 1];
               names[b + 1] = temp_name_holder;
            }
         }
      }

      if (N > 0) {
         int last_index = N - 1;
         String topName = names[last_index];
         int topScore = scores[last_index];

         if (topName == null) {
            topName = "";
         }

         System.out.println("Top student: " + topName + " (" + topScore + ")");
      }
   }
}