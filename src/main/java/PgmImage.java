import java.io.File;
import java.util.ArrayList;

/**
 * Created by CNOVA on 3/31/2016.
 */
public class PgmImage {
    String path, imgType;
    int imgWidth, imgHeight, maxIntensity;
    int imgMatrix[][];
    PgmImage(String path){
        this.path = path;
    }

    File sourcePgmFile;

    //variables to keep track of object dimensions in the image
    int imin, imax, jmin, jmax;

    //variables to store iSortedList, jSortedList, and isothetic cover list
    ArrayList<Vertex> iSortedList;
    ArrayList<Vertex> jSortedList;
    ArrayList<Vertex> isotheticVertices;

    /*2D list to save isothetic vertices in sorted i order, rows are sorted i order
    (smallest i in the topmost row and largest i in the bottom most row)
    and each row is sorted on j basis (smallest j in first, largest j in last)
    */
    ArrayList<ArrayList<Vertex>> iSorted2DList;

    /*2D list to save isothetic vertices in sorted j order, rows are sorted j order
    (smallest j in the topmost row and largest j in the bottom most row)
    and each row is sorted on i basis (smallest i in first, largest i in last)
    */
    ArrayList<ArrayList<Vertex>> jSorted2DList;

    ArrayList jAvgList;
    ArrayList iAvgList;


    //variables to store parameter scores
    int jAvg;
    int iAvg;

    //scoring variables
    int verticalSymmetry;
    int horizontalSymmetry;
    int horizontalTotalHeadSkip; //variable to keep track of total skips while traversing the cover to check for the symmetry
    int verticalTotalHeadSkip; //variable to keep track of total skips while traversing the cover to check for the symmetry
    int horizontalTotalTailSkip;
    int verticalTotalTailSkip;
    int horizontalMaxSkipStreak;
    int verticalMaxSkipStreak;
    double verticalAsymmetryProbability;
    double verticalSymmetryProbability;
    double horizontalAsymmetryProbability;
    double horizontalSymmetryProbability;
}
