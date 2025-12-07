import java.util.*;
public class Q3{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter grade: ");
int n=y.nextInt();
String a;
if(n>=90)a="A";
else if(n>=80)a="B";
else if(n>=70)a="C";
else if(n>=60)a="D";
else a="F";
System.out.println("Letter grade: "+a);
}
}