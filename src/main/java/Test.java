import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CNOVA on 4/2/2016.
 */
public class Test {
    private  static int d = 10;
    public static void main(String[] args) {
        File file = new File("E:\\learn\\academic\\final year project\\isothetic cover\\programs\\isothetic_cover\\src\\main\\resources\\A_raw_copy.pgm");
        System.out.println(file.getPath());
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
    }

    public static void increment(){
        d++;
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
<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> divye

    public static void printStringArrayContent(String[] list){
        for(String x: list){
            System.out.println(x);
        }
    }
<<<<<<< HEAD
=======
>>>>>>> c4b23e69e32a5e028ddb75f241d6d7be7453f631
=======

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
>>>>>>> divye
}
