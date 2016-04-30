import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CNOVA on 4/2/2016.
 */
public class Test {
    private  static int e = 20;
    public static void main(String[] args) {
        int d = 10;
        System.out.println(d);
        System.out.println(e);
        increment(d);
        System.out.println(d);
        System.out.println(e);
    }

    private static void increment(int d){
        d++;
        e++;
    }

    public static void printMatrix(int a[][]){
        System.out.println("number of rows in the matrix = " + a.length);
        for(int[] x: a){
            for(int y: x){
                System.out.print(y + " ");
            }
            System.out.println();
        }
    }

    public static void printList(List l){
        System.out.println("Size of the list is: " + l.size());
        Iterator iterator = l.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }

    public  static void printListContents(List l){
        System.out.println("Size of the list is: " + l.size());
        Iterator iterator = l.iterator();
        Vertex vertex;
        while(iterator.hasNext()){
            vertex = (Vertex)iterator.next();
            System.out.println(vertex.i + ",    " + vertex.j + ",   " + vertex.angle);
        }
    }

    public static void printStringArrayContent(String[] list){
        for(String x: list){
            System.out.println(x);
        }
    }

    public static void print2DListcontents(ArrayList<ArrayList<Vertex>> list2D){
        for(ArrayList<Vertex> list : list2D){
            for (Vertex vertex : list){
                System.out.print(vertex.i + "," + vertex.j + "," + vertex.angle + " | ");
            }
            System.out.println();
        }
    }

    public  static  void printVertex(Vertex vertex){
        System.out.println(vertex.i + ", " + vertex.j + ", " + vertex.angle);
    }
}
