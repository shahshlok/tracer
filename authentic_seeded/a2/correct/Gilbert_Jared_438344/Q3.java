import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int y = x.hasNextInt() ? x.nextInt() : -1;
        int n = y;
        if (n < 0) n = -1;
        if (n > 100) n = -1;
        String z = "";
        if (n >= 0) {
            if (n >= 90 && n <= 100) z = "A";
            else if (n >= 80 && n <= 89) z = "B";
            else if (n >= 70 && n <= 79) z = "C";
            else if (n >= 60 && n <= 69) z = "D";
            else z = "F";
        } else {
            z = "F";
        }
        System.out.println("Letter grade: " + z);
        x.close();
    }
}