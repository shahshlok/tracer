import java.util.Scanner;
public class Q3{
public static void main(String[] a){
Scanner s=new Scanner(System.in);
System.out.print("Enter grade: ");
int x=s.nextInt();
int y=x;
if(y<0)y=0;
if(y>100)y=100;
String n="";
if(y>=90&&y<=100)n="A";
if(y<90&&y>=80)n="B";
if(y<80&&y>=70)n="C";
if(y<70&&y>=60)n="D";
if(y<60&&y>=0)n="F";
if(n.length()>0)System.out.println("Letter grade: "+n);
}
}