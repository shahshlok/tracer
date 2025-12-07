import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
int s=0;
System.out.print("Enter 5 integers: ");
for(int n=0;n<5;n++){
int y=x.nextInt();
if(y%2==0)s+=y;
}
System.out.println("Sum of even numbers: "+s);
}
}