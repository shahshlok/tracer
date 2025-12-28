import java.util.Scanner;
import java.util.Arrays;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        int n = scanner.nextInt();

        if (n < 0) {
            n = 0;
        }

        String[] names = new String[n];
        int[] scores = new int[n];

        System.out.print("Enter names: ");
        int i = 0;
        while (i < n) {
            String tempName = scanner.next();
            names[i] = tempName;
            i++;
        }

        System.out.print("Enter scores: ");
        i = 0;
        while (i < n) {
            int tempScore = scanner.nextInt();
            scores[i] = tempScore;
            i++;
        }

        if (n > 1) {
            int[] tempScoresHolder = scores;
            Arrays.sort(tempScoresHolder);
            scores = tempScoresHolder;
        }

        if (n > 0) {
            String topName = names[n - 1];
            int topScore = scores[n - 1];
            System.out.println("Top student: " + topName + " (" + topScore + ")");
        }

        scanner.close();
    }
}