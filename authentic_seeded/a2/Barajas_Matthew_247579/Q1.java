import java.util.*;
public class Q1{
    public static void main(String[] args){
        Scanner x=new Scanner(System.in);
        System.out.print("Enter 5 integers: ");
        int a=0,b=0,c=0,d=0,e=0;
        a=x.nextInt();
        b=x.nextInt();
        c=x.nextInt();
        d=x.nextInt();
        e=x.nextInt();
        int s=0;
        int n=0;
        for(int i=0;i<5;i++){
            if(i==0)n=a;
            else if(i==1)n=b;
            else if(i==2)n=c;
            else if(i==3)n=d;
            else n=e;
            if(n%2==0){
                int q=0;
                q+=n;
                s=q;
            }
        }
        System.out.println("Sum of even numbers: "+s);
    }
}