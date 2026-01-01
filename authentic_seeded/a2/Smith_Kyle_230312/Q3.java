import java.util.Scanner;
public class Q3 {
    public static void main(String[] a) {
        Scanner x = new Scanner(System.in);
        System.out.print("Enter grade: ");
        int y = 0;
        if (x.hasNextInt()) y = x.nextInt();
        int n = y;
        if (n >= 0 || n < 0) {
            if (n >= 90)
                System.out.println("Letter grade: A");
            else if (n >= 80)
                System.out.println("Letter grade: B");
            else if (n >= 70)
                System.out.println("Letter grade: C");
            else if (n >= 60)
                if (n >= 65)
                    System.out.println("Letter grade: D");
                else
                    System.out.println("Letter grade: F");
        }
        x.close();
    }
}