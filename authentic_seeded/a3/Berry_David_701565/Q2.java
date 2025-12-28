import java.util.*;
public class Q2{
  public static void main(String[]a){
    Scanner s=new Scanner(System.in);
    System.out.print("Enter number of students: ");
    int n=s.nextInt();
    String[] x=new String[n];
    int[] y=new int[n];
    System.out.print("Enter names: ");
    for(int i=0;i<n;i++)x[i]=s.next();
    System.out.print("Enter scores: ");
    for(int i=0;i<n;i++)y[i]=s.nextInt();
    for(int i=0;i<n-1;i++)for(int j=0;j<n-1-i;j++)if(y[j]>y[j+1]){
      int t=y[j];
      y[j]=y[j+1];
      y[j+1]=t;
      String u=x[j];
      x[j]=x[j+1];
      x[j+1]=u;
    }
    System.out.println("Top student: "+x[n-1]+" ("+y[n-1]+")");
  }
}