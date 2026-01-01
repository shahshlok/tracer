import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int y = x.nextInt();
        int n = y;
        if (n < 0) {
            n = 0;
        }
        if (n > 100) {
            n = 100;
        }
        String z = "";
        if (n >= 90 && n <= 100) {
            z = "A";
        } else if (n >= 80 && n <= 89) {
            z = "B";
        } else if (n >= 70 && n <= 79) {
            z = "C";
        } else if (n >= 60 && n <= 69) {
            z = "D";
        } else {
            z = "F";
        }
        if (z.length() != 0) {
            System.out.println("Letter grade: " + z);
        } else {
            System.out.println("Letter grade: F");
        }
    }
}