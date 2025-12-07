import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter grade: ");
int x=s.nextInt();
char y;
if(x>=90)y='A';
else if(x>=80)y='B';
else if(x>=70)y='C';
else if(x>=60)y='D';
else y='F';
System.out.println("Letter grade: "+y);
}
}