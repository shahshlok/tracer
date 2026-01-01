import java.util.*;
public class Q1{
    public static void main(String[]a){
        Scanner s=new Scanner(System.in);
        System.out.print("Enter size: ");
        int n=s.nextInt();
        int[]x=new int[n];
        System.out.print("Enter elements: ");
        int i=0;
        while(i<n){
            x[i]=s.nextInt();
            i++;
        }
        System.out.print("Enter target: ");
        int t=s.nextInt();
        int y=-1;
        int j=0;
        if(n>0){
            while(j<n){
                int z=x[j];
                if(z==t){
                    y=j;
                    j=n;
                }else{
                    j++;
                }
            }
        }
        System.out.print("Found at index: ");
        System.out.print(y);
    }
}