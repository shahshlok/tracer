import java.util.*;
public class Q2{
    public static void main(String[] a){
        Scanner s=new Scanner(System.in);
        int n=0;
        System.out.print("Enter number of students: ");
        if(s.hasNextInt())n=s.nextInt();
        if(n<0)n=0;
        String[] x=new String[n];
        int[] y=new int[n];
        System.out.print("Enter names: ");
        int i=0;
        while(i<n){
            if(s.hasNext())x[i]=s.next();
            else x[i]="";
            i++;
        }
        System.out.print("Enter scores: ");
        i=0;
        while(i<n){
            if(s.hasNextInt())y[i]=s.nextInt();
            else y[i]=0;
            i++;
        }
        i=0;
        while(i<n){
            int j=i+1;
            while(j<n){
                int t=y[i];
                int u=y[j];
                if(u<t){
                    int v=y[i];
                    y[i]=y[j];
                    y[j]=v;
                    String p=x[i];
                    x[i]=x[j];
                    x[j]=p;
                }
                j++;
            }
            i++;
        }
        String r="";
        int q=0;
        if(n>0){
            r=x[n-1];
            q=y[n-1];
        }
        System.out.print("Top student: ");
        if(r!=null)System.out.print(r);
        else System.out.print("");
        System.out.print(" (");
        System.out.print(q);
        System.out.print(")");
    }
}