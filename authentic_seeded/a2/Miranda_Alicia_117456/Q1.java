import java.util.Scanner;

public class Q1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter 5 integers: ");
        int sum = 0;

        int count = 0;
        while (count < 5) {
            if (scanner.hasNextInt()) {
                int current = scanner.nextInt();
                int remainder = current % 2;
                if (remainder == 0) {
                    int newSum = sum + current;
                    sum = newSum;
                }
                int newCount = count + 1;
                count = newCount;
            } else {
                String skip = scanner.next();
            }
        }

        System.out.println("Sum of even numbers: " + sum);

        scanner.close();
    }
}