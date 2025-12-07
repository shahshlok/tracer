import java.util.Scanner;
public class Q3 {
    public static void main(String[] args) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int y = 0;
        if (x != null) {
            if (x.hasNextInt()) {
                y = x.nextInt();
            }
        }
        if (y < 0) {
            y = 0;
        }
        if (y > 100) {
            y = 100;
        }
        String n = "";
        if (y >= 90 && y <= 100) {
            n = "A";
        } else if (y >= 80 && y <= 89) {
            n = "B";
        } else if (y >= 70 && y <= 79) {
            n = "C";
        } else if (y >= 60 && y <= 69) {
            n = "D";
        } else {
            n = "F";
        }
        if (n != null) {
            System.out.println("Letter grade: " + n);
        }
        x.close();
    }
}