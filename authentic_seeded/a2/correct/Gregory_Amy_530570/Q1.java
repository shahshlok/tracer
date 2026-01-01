import java.util.*;
public class Q1{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
int y=0;
System.out.print("Enter 5 integers: ");
for(int n=0;n<5;n++){int z=x.nextInt();if(z%2==0)y+=z;}
System.out.println("Sum of even numbers: "+y);
}
}