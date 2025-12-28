import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        int n = scanner.nextInt();

        if (n <= 0) {
            System.out.println("Top student: (0)");
            scanner.close();
            return;
        }

        String[] names = new String[n];
        int[] scores = new int[n];

        System.out.print("Enter names: ");
        for (int i = 0; i < n; i++) {
            String tempName = scanner.next();
            names[i] = tempName;
        }

        System.out.print("Enter scores: ");
        for (int i = 0; i < n; i++) {
            int tempScore = scanner.nextInt();
            scores[i] = tempScore;
        }

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            int currentMinScore = scores[i];
            String currentMinName = names[i];

            for (int j = i + 1; j < n; j++) {
                int tempScore = scores[j];
                if (tempScore < currentMinScore) {
                    minIndex = j;
                    currentMinScore = scores[j];
                    currentMinName = names[j];
                }
            }

            if (minIndex != i) {
                int tempScoreHolder = scores[i];
                scores[i] = scores[minIndex];
                scores[minIndex] = tempScoreHolder;

                String tempNameHolder = names[i];
                names[i] = names[minIndex];
                names[minIndex] = tempNameHolder;
            }
        }

        String topName = names[n - 1];
        int topScore = scores[n - 1];

        if (topName == null) {
            topName = "";
        }

        System.out.println("Top student: " + topName + " (" + topScore + ")");

        scanner.close();
    }
}