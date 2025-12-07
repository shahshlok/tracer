import java.util.*;
public class Q1{
public static void main(String[]x){
Scanner n=new Scanner(System.in);
System.out.print("Enter 5 integers: ");
int a=0,b=0,c=0;
for(int y=0;y<5;y++){
a=n.nextInt();
b=a%2;
c+=b==0?a:0;
}
System.out.println("Sum of even numbers: "+c);
}
}