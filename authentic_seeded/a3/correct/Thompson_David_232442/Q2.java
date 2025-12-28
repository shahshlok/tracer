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
			
			int a = scores[j];
			int b = scores[j + 1];
			int c = a - b;

			if (c > 0) {
		    int tempScore = scores[j];
		    scores[j] = scores[j + 1];
		    scores[j + 1] = tempScore;

		    String tempName = names[j];
		    names[j] = names[j + 1];
		    names[j + 1] = tempName;
			}
     }
   }

   int idx_top = N - 1;
   String topName = names[idx_top];
   int topScore = scores[idx_top];

   System.out.println("Top student: " + topName + " (" + topScore + ")");
  }
}