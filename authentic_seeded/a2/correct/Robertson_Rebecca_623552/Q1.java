import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int x=0;
int y=0;
int n=0;
if(true){
n=s.nextInt();
if(n%2==0)y=y+n;
x=x+1;
}
if(x<5){
n=s.nextInt();
if(n%2==0)y=y+n;
x=x+1;
}
if(x<5){
n=s.nextInt();
if(n%2==0)y=y+n;
x=x+1;
}
if(x<5){
n=s.nextInt();
if(n%2==0)y=y+n;
x=x+1;
}
if(x<5){
n=s.nextInt();
if(n%2==0)y=y+n;
x=x+1;
}
if(x==5){
System.out.println("Sum of even numbers: "+y);
}
}
}