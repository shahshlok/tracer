import java.util.Scanner;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
int y=0;
int n=0;
System.out.print("Enter 5 integers: ");
for(int i=0;i<5;i++){
n=x.nextInt();
if(n%2==0){
int s=0;
s=s+n;
y=s;
}
}
System.out.println("Sum of even numbers: "+y);
}
}