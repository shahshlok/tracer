import java.util.Scanner;
public class Q4 {
public static void main(String[] args) {
Scanner x=new Scanner(System.in);
System.out.print("Enter height: ");
int n=x.nextInt();
for(int i=1;i<=n;i++){for(int j=1;j<=i;j++)System.out.print("*");System.out.println();}
}
}