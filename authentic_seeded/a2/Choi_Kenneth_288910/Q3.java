import java.util.*;
public class Q3{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
System.out.print("Enter grade: ");
int x=s.nextInt();
if(x>=90)
System.out.println("Letter grade: A");
else if(x>=80)
System.out.println("Letter grade: B");
else if(x>=70)
System.out.println("Letter grade: C");
else if(x>=60)
if(x>=65)
System.out.println("Letter grade: D");
else
System.out.println("Letter grade: F");
}
}