import java.util.Comparator;

/**
 * Created by CNOVA on 4/5/2016.
 */
public class VertexJComparator implements Comparator<Vertex> {
    @Override
    public int compare(Vertex v1, Vertex v2) {
        int result = 0;
        if (v1.j < v2.j)
            result = -1;
        else if (v1.j > v2.j)
            result = 1;
        else
            result = 0;
        return  result;
    }
}
