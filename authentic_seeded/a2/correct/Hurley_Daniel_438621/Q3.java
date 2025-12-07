import java.util.*;
public class Q3{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter grade: ");
int n=x.nextInt();
int a=90,b=80,c=70,d=60;
String y;
if(n>=a)y="A";
else if(n>=b)y="B";
else if(n>=c)y="C";
else if(n>=d)y="D";
else y="F";
System.out.println("Letter grade: "+y);
}
}