import java.util.Scanner;
public class Q3 {
    public static void main(String[] x) {
        Scanner s=new Scanner(System.in);
        System.out.print("Enter grade: ");
        int n=s.nextInt();
        String y;
        if(n>=90)y="A";
        else if(n>=80)y="B";
        else if(n>=70)y="C";
        else if(n>=60)y="D";
        else y="F";
        System.out.println("Letter grade: "+y);
    }
}