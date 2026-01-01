import java.util.Scanner;
import java.util.Random;
public class Q2{
public static void main(String[]a){
Scanner x=new Scanner(System.in);
Random y=new Random();
int n=y.nextInt(100)+1;
int g=0;
int c=0;
boolean d=false;
System.out.print("Guess a number (1-100): ");
if(x.hasNextInt()){
g=x.nextInt();
int h=g;
if(h==n){
c=c+1;
System.out.println("Correct! You took "+c+" guesses.");
d=true;
}
}
while(!d){
if(g!=0){
int h=g;
if(h==n){
c=c+1;
System.out.println("Correct! You took "+c+" guesses.");
d=true;
}else{
if(h>n){
c=c+1;
System.out.println("Too high!");
}else{
if(h<n){
c=c+1;
System.out.println("Too low!");
}
}
}
}else{
d=true;
}
}
x.close();
}
}