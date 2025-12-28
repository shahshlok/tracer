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

    int a = 0;
   int b = 0;
      int c = 0;

    for (int i = 0; i < N - 1; i++) {

      for (int j = 0; j < N - 1 - i; j++) {
         a = scores[j];
      b = scores[j + 1];
		  c = a - b;

        if (c > 0) {
            int temp_score = scores[j];
            scores[j] = scores[j + 1];
            scores[j + 1] = temp_score;

            String temp_name = names[j];
            names[j] = names[j + 1];
            names[j + 1] = temp_name;
        }
      }
    }

    
    int top_index = N - 1;
  	 String topName = names[top_index];
    int topScore = scores[top_index];

     System.out.println("Top student: " + topName + " (" + topScore + ")");
  }
}