import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        int n = scanner.nextInt();

        String[] names = new String[n];
        int[] scores = new int[n];

        System.out.print("Enter names: ");
        for (int i = 0; i < n; i++) {
            names[i] = scanner.next();
        }

        System.out.print("Enter scores: ");
        for (int i = 0; i < n; i++) {
            scores[i] = scanner.nextInt();
        }

        // Sort both arrays together based on scores (bubble sort)
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (scores[j] > scores[j + 1]) {
                    // Swap scores
                    int tempScore = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = tempScore;
                    // Swap names
                    String tempName = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = tempName;
                }
            }
        }

        // Print top student (last after ascending sort)
        System.out.println("Top student: " + names[n - 1] + " (" + scores[n - 1] + ")");
        scanner.close();
    }
}
