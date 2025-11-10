// Wilson David 789012
public class SumCalculator {
    public static void main(String[] args) {
        int totalSum = 0;
        int i = 1;
        while (i <= 100) {
            totalSum += i;
            i++;
        }
        System.out.println("The sum of numbers from 1 to 100 is: " + totalSum);
    }
}
