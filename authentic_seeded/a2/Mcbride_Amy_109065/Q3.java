import java.util.Scanner;
public class Q3{
public static void main(String[]x){
Scanner y=new Scanner(System.in);
System.out.print("Enter grade: ");
int n=y.nextInt();
if(n>=90)System.out.println("Letter grade: A");
else if(n>=80)System.out.println("Letter grade: B");
else if(n>=70)System.out.println("Letter grade: C");
else if(n>=60)System.out.println("Letter grade: D");
else System.out.println("Letter grade: F");
}
}