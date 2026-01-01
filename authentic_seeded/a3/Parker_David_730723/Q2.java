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
      for (int i = 0; i < N; i++) {
         if (sc.hasNext()) {
            String temp_name = sc.next();
            names[i] = temp_name;
         } else {
            names[i] = "";
         }
      }

      System.out.print("Enter scores: ");
      for (int j = 0; j < N; j++) {
         int temp_score = 0;
         if (sc.hasNextInt()) {
            temp_score = sc.nextInt();
         }
         scores[j] = temp_score;
      }


      // sort parallel arrays by scores ascending (simple bubble sort)
      if (N > 1) {
      	for (int i = 0; i < N - 1; i++) {
      		for (int k = 0; k < N - 1 - i; k++) {
      			int current_score = scores[k];
      			int next_score = scores[k + 1];

      			if (current_score > next_score) {
      				int temp_s = scores[k];
      				scores[k] = scores[k + 1];
      				scores[k + 1] = temp_s;

      				String temp_n = names[k];
      				names[k] = names[k + 1];
      				names[k + 1] = temp_n;
      			}
      		}
      	}
      }

      
      if (N > 0) {
      	int last_index = N - 1;
      	String top_name = names[last_index];
      	int top_score = scores[last_index];

      	if (top_name == null) {
      		top_name = "";
      	}

      	System.out.println("Top student: " + top_name + " (" + top_score + ")");
      } else {
      	// no students, but still behave safely
      	System.out.println("Top student:  ()");
      }

      sc.close();
   }
}