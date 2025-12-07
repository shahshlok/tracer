import java.util.Scanner;
public class Q3{
public static void main(String[] args){
Scanner x=new Scanner(System.in);
System.out.print("Enter grade: ");
int y=0;
if(x.hasNextInt())y=x.nextInt();
String n="";
if(y>=0&&y<=100){
int z=y;
if(z>=90&&z<=100)n="A";
else if(z>=80&&z<=89)n="B";
else if(z>=70&&z<=79)n="C";
else if(z>=60&&z<=69)n="D";
else if(z<60)n="F";
}else{
n="";
}
if(n!=""){System.out.println("Letter grade: "+n);}
}}