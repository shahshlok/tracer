import java.util.*;
public class Q4{
    public static void main(String[] a){
        Scanner x=new Scanner(System.in);
        System.out.print("Enter height: ");
        int n=0;
        if(x.hasNextInt())n=x.nextInt();
        if(n<0)n=0;
        int y=1;
        while(y<=n){
            int z=0;
            int t=y;
            if(t<0)t=0;
            while(z<t){
                System.out.print("*");
                z=z+1;
            }
            System.out.println();
            y=y+1;
        }
    }
}