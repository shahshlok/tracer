import java.util.*;

public class Q2 {
  public static void main(String[] args) {
   Scanner in = new Scanner(System.in);

    System.out.print("Enter number of students: ");
    int n = in.nextInt();

    String[] names = new String[n];
      int[] scores_array = new int[n];

    System.out.print("Enter names: ");
    for (int i = 0; i < n; i++) {
      names[i] = in.next();
    }

    System.out.print("Enter scores: ");
    	 for (int i = 0; i < n; i++) {
      scores_array[i] = in.nextInt();
    }



    for (int i = 0;   i < n - 1; i++) {
       for (int j = 0; j < n - 1 - i; j++) {
        if (scores_array[j] > scores_array[j + 1]) {
         int temp_score = scores_array[j];
          scores_array[j] = scores_array[j + 1];
           scores_array[j + 1] = temp_score;

          String temp_name = names[j];
          names[j] = names[j + 1];
           names[j + 1] = temp_name;
        }
      }
    }

     String topName = names[n - 1];
  int topScore = scores_array[n - 1];

    System.out.println("Top student: " + topName + " (" + topScore + ")");
  }
}