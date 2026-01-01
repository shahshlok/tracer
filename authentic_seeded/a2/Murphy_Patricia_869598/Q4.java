import java.util.Scanner;
public class Q4{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter height: ");
int n=s.nextInt();
if(n<0)n=0;
int x=1;
while(x<=n){
int y=1;
String z="";
while(y<=x){
z=z+"*";
y=y+1;
}
if(z.length()>0)System.out.println(z);
x=x+1;
}
}
}