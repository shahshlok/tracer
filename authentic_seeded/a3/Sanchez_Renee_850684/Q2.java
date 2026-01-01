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


   Arrays.sort(scores);



	if (N > 0) {
      String topName = names[N - 1];
      int topScore = scores[N - 1];
      System.out.println("Top student: " + topName + " (" + topScore + ")");
	}

    sc.close();
  }
}