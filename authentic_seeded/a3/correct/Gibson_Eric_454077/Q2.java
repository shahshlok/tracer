import java.util.*;

public class Q2 {
  public static void main(String[] args) {
   Scanner sc = new Scanner(System.in);

    System.out.print("Enter number of students: ");
    int N = sc.nextInt();

      String[] names = new String[N];
   int[] scores = new int[N];

    System.out.print("Enter names: ");
    for (int i = 0; i < N; i++) {
       names[i] = sc.next();
    }

	 System.out.print("Enter scores: ");
    for (int i = 0; i < N; i++) {
      scores[i] = sc.nextInt();
    }


    for (int i = 0; i < N - 1; i++) {
       for (int j = 0; j < N - 1 - i; j++) {
          if (scores[j] > scores[j + 1]) {
            int temp_score = scores[j];
            scores[j] = scores[j + 1];
            scores[j + 1] = temp_score;

            String tempName = names[j];
             names[j] = names[j + 1];
             names[j + 1] = tempName;
          }
       }
    }

    String topName = names[N - 1];
      int top_score = scores[N - 1];

    System.out.println("Top student: " + topName + " (" + top_score + ")");
  }
}