import java.util.Scanner;

public class Q2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of students: ");
        int n = sc.nextInt();

        String[] names = new String[n];
        int[] scores = new int[n];

        System.out.print("Enter names: ");
        for (int i = 0; i < n; i++) {
            names[i] = sc.next();
        }

        System.out.print("Enter scores: ");
        for (int i = 0; i < n; i++) {
            scores[i] = sc.nextInt();
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                int a = scores[j];
                int b = scores[j + 1];
                if (a > b) {
                    int tempScore = scores[j];
                    scores[j] = scores[j + 1];
                    scores[j + 1] = tempScore;

                    String tempName = names[j];
                    names[j] = names[j + 1];
                    names[j + 1] = tempName;
                }
            }
        }

        int indexTop = n - 1;
        String topName = names[indexTop];
        int topScore = scores[indexTop];

        System.out.println("Top student: " + topName + " (" + topScore + ")");
    }
}