import java.util.Scanner;
public class Q1{
    public static void main(String[]a){
        Scanner x=new Scanner(System.in);
        System.out.print("Enter 5 integers: ");
        int s=0;
        int n=0;
        int y=0;
        n=x.nextInt();
        if(n%2==0||n%2!=1)s=s+n;
        y=x.nextInt();
        if(y%2==0||y%2!=1)s=s+y;
        n=x.nextInt();
        if(n%2==0||n%2!=1)s=s+n;
        y=x.nextInt();
        if(y%2==0||y%2!=1)s=s+y;
        n=x.nextInt();
        if(n%2==0||n%2!=1)s=s+n;
        System.out.println("Sum of even numbers: "+s);
    }
}