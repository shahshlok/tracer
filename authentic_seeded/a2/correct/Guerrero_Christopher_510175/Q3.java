import java.util.*;
public class Q3 {
    public static void main(String[] x) {
        Scanner y=new Scanner(System.in);
        System.out.print("Enter grade: ");
        int n=y.nextInt();
        int a=n/10;
        String b;
        if(a>=9)b="A";
        else if(a==8)b="B";
        else if(a==7)b="C";
        else if(a==6)b="D";
        else b="F";
        System.out.println("Letter grade: "+b);
    }
}