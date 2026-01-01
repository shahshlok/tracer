import java.util.Scanner;
public class Q4 {
    public static void main(String[] a) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter three points for a triangle.");
        System.out.print("(x1, y1):");
        String x = s.nextLine();
        int n = x.length();
        double x1 = 0;
        double y1 = 0;
        if (n != 0) {
            String y = x;
            int i = 0;
            if (i < n) {
                while (i < n && Character.isWhitespace(y.charAt(i))) i++;
                int j = i;
                while (j < n && (Character.isDigit(y.charAt(j)) || y.charAt(j) == '.' || y.charAt(j) == '-' || y.charAt(j) == '+')) j++;
                String t = y.substring(i, j);
                if (t.length() != 0) x1 = Double.parseDouble(t);
                i = j;
                while (i < n && Character.isWhitespace(y.charAt(i))) i++;
                j = i;
                while (j < n && (Character.isDigit(y.charAt(j)) || y.charAt(j) == '.' || y.charAt(j) == '-' || y.charAt(j) == '+')) j++;
                t = "";
                if (j > i) t = y.substring(i, j);
                if (t.length() != 0) y1 = Double.parseDouble(t);
            }
        }
        System.out.print("(x2, y2):");
        x = s.nextLine();
        n = x.length();
        double x2 = 0;
        double y2 = 0;
        if (n != 0) {
            String y = x;
            int i = 0;
            if (i < n) {
                while (i < n && Character.isWhitespace(y.charAt(i))) i++;
                int j = i;
                while (j < n && (Character.isDigit(y.charAt(j)) || y.charAt(j) == '.' || y.charAt(j) == '-' || y.charAt(j) == '+')) j++;
                String t = y.substring(i, j);
                if (t.length() != 0) x2 = Double.parseDouble(t);
                i = j;
                while (i < n && Character.isWhitespace(y.charAt(i))) i++;
                j = i;
                while (j < n && (Character.isDigit(y.charAt(j)) || y.charAt(j) == '.' || y.charAt(j) == '-' || y.charAt(j) == '+')) j++;
                t = "";
                if (j > i) t = y.substring(i, j);
                if (t.length() != 0) y2 = Double.parseDouble(t);
            }
        }
        System.out.print("(x3, y3):");
        x = s.nextLine();
        n = x.length();
        double x3 = 0;
        double y3 = 0;
        if (n != 0) {
            String y = x;
            int i = 0;
            if (i < n) {
                while (i < n && Character.isWhitespace(y.charAt(i))) i++;
                int j = i;
                while (j < n && (Character.isDigit(y.charAt(j)) || y.charAt(j) == '.' || y.charAt(j) == '-' || y.charAt(j) == '+')) j++;
                String t = y.substring(i, j);
                if (t.length() != 0) x3 = Double.parseDouble(t);
                i = j;
                while (i < n && Character.isWhitespace(y.charAt(i))) i++;
                j = i;
                while (j < n && (Character.isDigit(y.charAt(j)) || y.charAt(j) == '.' || y.charAt(j) == '-' || y.charAt(j) == '+')) j++;
                t = "";
                if (j > i) t = y.substring(i, j);
                if (t.length() != 0) y3 = Double.parseDouble(t);
            }
        }
        double d1 = 0;
        double d2 = 0;
        if (x1 != x2 || y1 != y2) {
            double u = x2 - x1;
            double v = y2 - y1;
            double w = u * u;
            double z = v * v;
            double q = w + z;
            if (q >= 0) d1 = Math.sqrt(q);
        }
        if (x2 != x3 || y2 != y3) {
            double u = x3 - x2;
            double v = y3 - y2;
            double w = u * u;
            double z = v * v;
            double q = w + z;
            if (q >= 0) d2 = Math.sqrt(q);
        }
        double d3 = 0;
        if (x1 != x3 || y1 != y3) {
            double u = x3 - x1;
            double v = y3 - y1;
            double w = u * u;
            double z = v * v;
            double q = w + z;
            if (q >= 0) d3 = Math.sqrt(q);
        }
        double p = d1 + d2 + d3;
        double h = 0;
        if (p != 0) h = d1 + d2 + d3 / 2.0;
        double r = h - d1;
        double t = h - d2;
        double k = h - d3;
        double m = h * r * t * k;
        double area = 0;
        if (m > 0) area = Math.sqrt(m);
        System.out.println("The area of the triangle is " + area);
    }
}