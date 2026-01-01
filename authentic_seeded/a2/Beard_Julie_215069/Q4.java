import java.util.Scanner;
public class Q4{
public static void main(String[]args){
Scanner x=new Scanner(System.in);
System.out.print("Enter height: ");
int n=x.nextInt();
for(int y=1;y<=n;y++){
for(int z=1;z<=y;z++)System.out.print("*");y=0;
System.out.println();
}
}
}