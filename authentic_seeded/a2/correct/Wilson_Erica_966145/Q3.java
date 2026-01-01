import java.util.*;
public class Q3{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
int n=y.nextInt();
int a=90,b=80,c=70,d=60;
String s;
if(n>=a)s="A";
else if(n>=b)s="B";
else if(n>=c)s="C";
else if(n>=d)s="D";
else s="F";
System.out.println("Letter grade: "+s);
}
}