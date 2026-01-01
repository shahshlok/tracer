import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter grade: ");
int n=0;
if(x.hasNextInt())n=x.nextInt();
String y="";
if(n>=0){
if(n>=90&&n<=100)y="A";else if(n>=80&&n<=89)y="B";else if(n>=70&&n<=79)y="C";else if(n>=60&&n<=69)y="D";else if(n<60)y="F";
}
if(y.equals(""))y="F";
System.out.println("Letter grade: "+y);
}
}