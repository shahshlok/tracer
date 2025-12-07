import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int x = s.nextInt();
        int y = x;
        String n = "F";
        if (y >= 0) {
            if (y >= 60) {
                if (y >= 70) {
                    if (y >= 80) {
                        if (y >= 90) {
                            if (y <= 100) {
                                n = "A";
                            } else {
                                n = "A";
                            }
                        } else {
                            n = "B";
                        }
                    } else {
                        n = "C";
                    }
                } else {
                    n = "D";
                }
            } else {
                n = "F";
            }
        } else {
            n = "F";
        }
        System.out.println("Letter grade: " + n);
    }
}