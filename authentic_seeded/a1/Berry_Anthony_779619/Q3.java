import java.util.Scanner;
public class Q3{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
System.out.print("Enter x1 and y1: ");
int x1=x.nextInt(),y1=x.nextInt();
System.out.print("Enter x2 and y2: ");
int x2=x.nextInt(),y2=x.nextInt();
double n=Math.sqrt((x2-x1)^(2)+(y2-y1)^(2));
System.out.println("The distance of the two points is "+n);
}
}