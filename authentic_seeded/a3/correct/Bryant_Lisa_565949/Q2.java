import java.util.*;
public class Q2{
    public static void main(String[] args){
        Scanner x=new Scanner(System.in);
        System.out.print("Enter number of students: ");
        int n=0;
        if(x.hasNextInt())n=x.nextInt();
        if(n<0)n=0;
        String[] y=new String[n];
        int[] z=new int[n];
        System.out.print("Enter names: ");
        int i=0;
        while(i<n){
            String t="";
            if(x.hasNext())t=x.next();
            y[i]=t;
            i++;
        }
        System.out.print("Enter scores: ");
        i=0;
        while(i<n){
            int t=0;
            if(x.hasNextInt())t=x.nextInt();
            z[i]=t;
            i++;
        }
        i=0;
        while(i<n){
            int j=i+1;
            while(j<n){
                int a=z[i];
                int b=z[j];
                if(a>b){
                    int tt=z[i];
                    z[i]=z[j];
                    z[j]=tt;
                    String ts=y[i];
                    y[i]=y[j];
                    y[j]=ts;
                }
                j++;
            }
            i++;
        }
        String rName="";
        int rScore=0;
        if(n>0){
            rName=y[n-1];
            rScore=z[n-1];
        }
        System.out.print("Top student: ");
        if(rName!=null)System.out.print(rName);
        System.out.print(" (");
        System.out.print(rScore);
        System.out.print(")");
    }
}