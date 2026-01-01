import java.util.Scanner;
public class Q3{
public static void main(String[] args){
Scanner x=new Scanner(System.in);
System.out.print("Enter grade: ");
int y=x.nextInt();
int n=y;
String z="";
if(n>=0){
if(n>=90&&n<=100)z="A";
if(z.equals("")&&n>=80&&n<=89)z="B";
if(z.equals("")&&n>=70&&n<=79)z="C";
if(z.equals("")&&n>=60&&n<=69)z="D";
if(z.equals("")&&n<60&&n>=0)z="F";
}
if(z.equals("")&&n<0)z="F";
System.out.println("Letter grade: "+z);
}
}