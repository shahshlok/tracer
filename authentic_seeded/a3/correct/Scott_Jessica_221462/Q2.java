import java.util.Scanner;

public class Q2 {

  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

      System.out.print("Enter number of students: ");
      int N = 0;
      if (sc.hasNextInt()) {
          N = sc.nextInt();
      }

      if (N <= 0) {
         // nothing to process, but avoid crash
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

      
      for (int i_i = 0; i_i < N - 1; i_i++) {
        for (int j_j = 0; j_j < N - 1 - i_i; j_j++) {
           int curScore = scores[j_j];
           int nextScore = scores[j_j + 1];

           if (nextScore < curScore) {
              int tmpScore = scores[j_j];
              scores[j_j] = scores[j_j + 1];
              scores[j_j + 1] = tmpScore;

              String tmpName = names[j_j];
              names[j_j] = names[j_j + 1];
              names[j_j + 1] = tmpName;
           }
        }
      }

      
      int topIndex = N - 1;
      if (topIndex >= 0) {
        String topName = names[topIndex];
        int top_score = scores[topIndex];

        System.out.println("Top student: " + topName + " (" + top_score + ")");
      }

      sc.close();
  }
}