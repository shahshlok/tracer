import java.util.Random;
import java.util.Scanner;
public class Q2{
public static void main(String[] a){
Scanner x=new Scanner(System.in);
Random y=new Random();
int n=y.nextInt(100)+1;
int g=0;
int c=0;
boolean b=false;
while(b==false){
System.out.print("Guess a number (1-100): ");
if(x.hasNextInt()){
g=x.nextInt();
}else{
x.next();
g=0;
}
if(g>=1&&g<=100){
c=c+1;
if(g==n){
b=true;
}else{
if(g>n){
System.out.println("Too high!");
}else{
if(g<n){
System.out.println("Too low!");
}
}
}
}else{
String s="";
s=s+"";
}
}
System.out.println("Correct! You took "+c+" guesses.");
}
}