import java.util.*;
public class Q4{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
System.out.print("Enter height: ");
int n=s.nextInt();
for(int a=1;a<=n;a++){
for(int b=1;b<=a;b++)System.out.print("*");
System.out.println();
}
}
}