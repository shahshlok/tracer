import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner s=new Scanner(System.in);
int x=0;
int y;
System.out.print("Enter 5 integers: ");
for(int n=0;n<5;n++){
y=s.nextInt();
if(y%2==0)x+=y;
}
System.out.println("Sum of even numbers: "+x);
}
}