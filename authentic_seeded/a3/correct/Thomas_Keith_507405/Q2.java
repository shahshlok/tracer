import java.util.*;

public class Q2 {
   public static void main(String[] args){
      Scanner sc = new Scanner(System.in);
      
      System.out.print("Enter number of students: ");
      int N = sc.nextInt();

      
      String[] names = new String[N];
         int[] scores = new int[N];

      System.out.print("Enter names: ");
      for(int i = 0; i < N; i++){
      	names[i] = sc.next();
      }

      System.out.print("Enter scores: ");
      for(int i = 0; i < N; i++){
      	scores[i] = sc.nextInt();
      }

      
      for (int i = 0; i < N - 1; i++) {
      	 int a = i;
         int b = i + 1;
         	int c = N;

         for (int j = a; j < c - 1; j++) {
         	   int d = j + 1;
            if (scores[j] > scores[d]) {
            	int temp_score = scores[j];
            	scores[j] = scores[d];
            	scores[d] = temp_score;

            	String temp_name = names[j];
            	names[j] = names[d];
            	names[d] = temp_name;
            }
         }
      }

      
      int idx_top = N - 1;
      int top_score = scores[idx_top];
      String top_name = names[idx_top];

      System.out.println("Top student: " + top_name + " (" + top_score + ")");
   }
}