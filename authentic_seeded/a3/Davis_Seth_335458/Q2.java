import java.util.*;

public class Q2 {
  public static void main(String[] args) {
      Scanner sc = new Scanner(System.in);

    System.out.print("Enter number of students: ");
      int n = sc.nextInt();

    String[] names = new String[n];
      int[] scores = new int[n];

   System.out.print("Enter names: ");
      for (int i = 0; i < n; i++) {
         names[i] = sc.next();
      }

	System.out.print("Enter scores: ");
      for (int i = 0; i < n; i++) {
         scores[i] = sc.nextInt();
      }


      for (int i = 0; i < n - 1; i++) {
         for (int j = 0; j < n - 1 - i; j++) {
            if (scores[j] > scores[j + 1]) {
               int temp_score = scores[j];
               scores[j] = scores[j + 1];
               scores[j + 1] = temp_score;

               String temp_name = names[j];
               names[j] = names[j + 1];
               names[j + 1] = temp_name;
            }
         }
      }

   String topName = names[n - 1];
      int topScore = scores[n - 1];

      System.out.println("Top student: " + topName + " (" + topScore + ")");
  }
}