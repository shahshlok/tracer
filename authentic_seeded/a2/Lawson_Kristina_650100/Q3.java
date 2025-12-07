import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int y = x.nextInt();
        int n = y;
        if (n < 0) n = 0;
        if (n > 100) n = 100;
        String s = "";
        if (n >= 90 && n <= 100) s = "A";
        else if (n >= 80 && n <= 89) s = "B";
        else if (n >= 70 && n <= 79) s = "C";
        else if (n >= 60 && n <= 69) s = "D";
        else s = "F";
        System.out.println("Letter grade: " + s);
        x.close();
    }
}