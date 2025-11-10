// Davis Michael 567890
public class SumCalculator {
    public static void main(String[] args) {
        int evenSum = 0;
        for (int i = 1; i <= 100; i++) {
            if (i % 2 == 0) {
                evenSum += i;
            }
        }
        System.out.println("The sum of numbers from 1 to 100 is: " + evenSum);
    }
}
