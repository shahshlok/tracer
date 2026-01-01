import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        double x = 0;
        double y = 0;
        double n = 0;
        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        String t = s.nextLine();
        if (t != null) {
            if (t.length() > 0) {
                double x1 = 0;
                double y1 = 0;
                int i = 0;
                while (i < t.length()) {
                    char c = t.charAt(i);
                    if (c == ' ' || c == ',') {
                        String u = t.substring(0, i);
                        if (u.length() > 0) x1 = Double.parseDouble(u);
                        String v = t.substring(i + 1);
                        if (v.length() > 0) y1 = Double.parseDouble(v);
                        i = t.length();
                    } else {
                        i++;
                    }
                }
                if (t.indexOf(' ') < 0 && t.indexOf(',') < 0) x1 = Double.parseDouble(t);
                System.out.print("(x2, y2):");
                String t2 = s.nextLine();
                double x2 = 0;
                double y2 = 0;
                int j = 0;
                if (t2 != null) {
                    while (j < t2.length()) {
                        char c2 = t2.charAt(j);
                        if (c2 == ' ' || c2 == ',') {
                            String u2 = t2.substring(0, j);
                            if (u2.length() > 0) x2 = Double.parseDouble(u2);
                            String v2 = t2.substring(j + 1);
                            if (v2.length() > 0) y2 = Double.parseDouble(v2);
                            j = t2.length();
                        } else {
                            j++;
                        }
                    }
                    if (t2.indexOf(' ') < 0 && t2.indexOf(',') < 0) x2 = Double.parseDouble(t2);
                }
                System.out.print("(x3, y3):");
                String t3 = s.nextLine();
                double x3 = 0;
                double y3 = 0;
                int k = 0;
                if (t3 != null) {
                    while (k < t3.length()) {
                        char c3 = t3.charAt(k);
                        if (c3 == ' ' || c3 == ',') {
                            String u3 = t3.substring(0, k);
                            if (u3.length() > 0) x3 = Double.parseDouble(u3);
                            String v3 = t3.substring(k + 1);
                            if (v3.length() > 0) y3 = Double.parseDouble(v3);
                            k = t3.length();
                        } else {
                            k++;
                        }
                    }
                    if (t3.indexOf(' ') < 0 && t3.indexOf(',') < 0) x3 = Double.parseDouble(t3);
                }
                double dx = x2 - x1;
                double dy = y2 - y1;
                double side1 = Math.sqrt(dx * dx + dy * dy);
                double dx2 = x3 - x2;
                double dy2 = y3 - y2;
                double side2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);
                double dx3 = x1 - x3;
                double dy3 = y1 - y3;
                double side3 = Math.sqrt(dx3 * dx3 + dy3 * dy3);
                double per = side1 + side2 + side3;
                double semi = per / 2.0;
                double temp = semi * (semi - side1) * (semi - side2) * (semi - side3);
                double area = 0;
                if (temp >= 0) area = Math.sqrt(temp);
                System.out.println("The area of the triangle is " + area);
            }
        }
        s.close();
    }
}