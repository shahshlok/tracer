import java.util.*;
public class Q3{
public static void main(String[]x){
Scanner s=new Scanner(System.in);
int n=s.nextInt();
int a=n/10;
String y;
if(a>=9)y="A";
else if(a==8)y="B";
else if(a==7)y="C";
else if(a==6)y="D";
else y="F";
System.out.println("Letter grade: "+y);
}
}