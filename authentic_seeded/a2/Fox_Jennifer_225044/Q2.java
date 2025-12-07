import java.util.*;
public class Q2{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
Random y=new Random();
int a=y.nextInt(100)+1;
int b=0;
int c=0;
System.out.print("Guess a number (1-100): ");
b=x.nextInt();
while(b!=a){
c++;
if(b<a)System.out.println("Too low!");
else if(b>a)System.out.println("Too high!");
}
System.out.println("Correct! You took "+c+" guesses.");
}
}