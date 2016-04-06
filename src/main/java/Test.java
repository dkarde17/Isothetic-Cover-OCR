import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CNOVA on 4/2/2016.
 */
public class Test {
    private  static int d = 10;
    public static void main(String[] args) {
        /*FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("E:\\learn\\academic\\final year project\\isothetic cover\\programs\\isothetic_cover\\src\\main\\resources\\abcd.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DataInputStream dataInputStream = new DataInputStream(fileInputStream);
        try {
            int i = 0;
            String line = null;
            while (dataInputStream.available() != 0){
                line = dataInputStream.readLine();
                System.out.println(line);
                System.out.println(i++);
                System.out.println(line.length());
                System.out.println(line.indexOf('#'));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        int[][] a = new int[10][10];

        printMatrix(a);

        for (int[] b: a){
            for (int x: b){
                x = 1;
            }
        }
        printMatrix(a);

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
}
