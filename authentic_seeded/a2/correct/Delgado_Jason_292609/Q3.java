import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter grade: ");
int n=x.nextInt();
int b=90,c=80,d=70,e=60;
String y="";
if(n>=b)y="A";
else if(n>=c)y="B";
else if(n>=d)y="C";
else if(n>=e)y="D";
else y="F";
System.out.println("Letter grade: "+y);
}
}