import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 4/3/2016.
 */
public class IsotheticCoverCalc {

    //flag to get the direction of the outgoing edge from first 90 degree vertex
    private static int startDirection = 0;

    //flag to get the direction of each edge in nextVertex() method, 1 for north, 2 for east, 3 for south and 4 for west
    private static int direction;

    //flag to keep track of the first (starting)) 90 degree vertex (C1 vertex)
    private static int c1flag = 1;

    //method to create raw unitEdgeMatrix according to the imgMatrix dimensions and grid Size
    public static int[][] createUnitEdgeMatrix(PgmImage pgmImage, Properties properties){

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        System.out.println("Grid size = " + gridSize);

        //number of rows in the UnitEdgeMatrix depends on the mapping i co-ordinate of the last horizontal edge row (i = imgHeight) of the grid to UnitEdgeMatrix
        int rowMax = iMappingHorizontalEdge(pgmImage.imgHeight, gridSize);

        //number of columns in the UnitEdgeMatrix depends on the mapping i co-ordinate of the last vertical edge column (i = imgWidth) of the grid to UnitEdgeMatrix
        int colMax = jMapping(pgmImage.imgWidth, gridSize);

        //creating the raw UnitEdgeMatrix
        int[][] unitEdgeMatrix = new int[rowMax+1][colMax+1]; //adding 1 because index starts from 0

        return unitEdgeMatrix;
    }

    /*
    Method to set (0 or 1 in) the unitEdgeMatrix according to the information in the imgMatrix
     */
    public static void setUnitEdgeMatrix(int[][] unitEdgeMatrix, PgmImage pgmImage, Properties properties){

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        System.out.println("Grid size = " + gridSize);

        /*
        For each intersection point on the grid i.e. vertex <iVertex, jVertex> check if the horizontal or vertical edge or both
        are being intersected by the image or not, fill 1 in the corresponding cell of the unitEdgeMatrix if intersecting, otherwise 0
        iVertex and jVertex are multiples of gridSize, except for the rightmost and the bottommost vertices, because the index of the
        imgMatrix starts from 0
         */
        for(int iVertex = 0; iVertex <= pgmImage.imgHeight; iVertex+=gridSize){
            for(int jVertex = 0; jVertex <= pgmImage.imgWidth; jVertex+=gridSize){

                //all vertices except rightmost and bottommost vertices
                if(iVertex < pgmImage.imgHeight && jVertex < pgmImage.imgWidth){

                    //check for the horizontal edge
                    for(int j = jVertex; j < jVertex + gridSize; j++){
                        if (pgmImage.imgMatrix[iVertex][j] == 0){
                            unitEdgeMatrix[iMappingHorizontalEdge(iVertex, gridSize)][jMapping(jVertex, gridSize)] = 1;
                            break;
                        }
                    }

                    //check for the vertical edge
                    for(int i = iVertex; i < iVertex + gridSize; i++){
                        if (pgmImage.imgMatrix[i][jVertex] == 0){
                            unitEdgeMatrix[iMappingVerticalEdge(iVertex, gridSize)][jMapping(jVertex, gridSize)] = 1;
                            break;
                        }
                    }
                }

                //rightmost vertices
                else if (iVertex < pgmImage.imgHeight && jVertex == pgmImage.imgWidth){
                    //check for the vertical edge
                    for(int i = iVertex; i < iVertex + gridSize; i++){
                        if (pgmImage.imgMatrix[i][jVertex-1] == 0){
                            unitEdgeMatrix[iMappingVerticalEdge(iVertex, gridSize)][jMapping(jVertex, gridSize)] = 1;
                            break;
                        }
                    }
                }

                //bottommost vertices
                else if (iVertex == pgmImage.imgWidth && jVertex < pgmImage.imgWidth){
                    //check for the horizontal matrix
                    for(int j = jVertex; j < jVertex + gridSize; j++){
                        if (pgmImage.imgMatrix[iVertex-1][j] == 0){
                            unitEdgeMatrix[iMappingHorizontalEdge(iVertex, gridSize)][jMapping(jVertex, gridSize)] = 1;
                            break;
                        }
                    }
                }
            }
        }
    }

    //method to create the raw unitSquareMatrix
    public static int[][] createUnitSquareMatrix(PgmImage pgmImage, Properties properties){

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        System.out.println("Grid size = " + gridSize);

        //no. of rows and columns depends on the image size and the grid size
        int rowMax = pgmImage.imgHeight/gridSize;
        int colMax = pgmImage.imgWidth/gridSize;

        int[][] unitSquareMatrix = new int[rowMax][colMax];

        return unitSquareMatrix;
    }

    //method to set (0 or 1 in) the unitSquareMatrix according to the information in teh unitEdgeMatrix
    public static void setUnitSquareMatrix(int[][] unitSquareMatrix, int[][] unitEdgeMatrix){

        //no. of rows in the unitSquareMatrix
        int rowMax = unitSquareMatrix.length;

        //no. of columns in the unitSquareMatrix
        int colMax = unitSquareMatrix[0].length;

        /*
        For each square (cell) with co-ordinates <is, js> in the unitSquareMatrix, check if it contains a part of the image or not
        for the square to contain image at least one of its four edges should be intersected by the image. The four images of the square are:
        unitEdgeMatrix[2*is][js]
        unitEdgeMatrix[2*is+1][js]
        unitEdgeMatrix[2*is+1][js+1]
        unitEdgeMatrix[2*is+2][js]
        because is = iVertex/gridSize and js = jVertex/gridSize
         */
        for (int row = 0; row < rowMax; row++){
            for (int col = 0; col < colMax; col++){

                //check for the four edges
                if(unitEdgeMatrix[2*row][col] == 1 || unitEdgeMatrix[2*row + 1][col] == 1 || unitEdgeMatrix[2*row + 1][col + 1] == 1 || unitEdgeMatrix[2*row + 2][col] == 1)
                    unitSquareMatrix[row][col] = 1;

            }
        }
    }

    //method to find out the Isothetic cover vertices
    public static ArrayList<Vertex> listVertices(PgmImage pgmImage, int[][] unitSquareMatrix, Properties properties, Logger LOGGER){
        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        System.out.println("Grid size = " + gridSize);
        int iMax = pgmImage.imgHeight/gridSize;
        int jMax = pgmImage.imgWidth/gridSize;

        //list to store the vertices, sorted in increasing order with primary key i and secondary key j
        ArrayList<Vertex> iSortedList = new ArrayList<>();


        //list to store the vertices, sorted in increasing order with primary key j and secondary key i
        ArrayList<Vertex> jSortedList;

        //list to store the vertices in the Isothetic polygon in the order of traversal starting from the start Vertex
        LOGGER.info("Creating list to store the vertices of isothetic polygon in traversal order");
        ArrayList<Vertex> isotheticVertices = new ArrayList<>();
        LOGGER.info("isotheticVertices list successfully created");

        Vertex vertex;
        Vertex start = null;
        Vertex temp;


        LOGGER.info("Creating list with vertices sorted in increasing order with i as primary key and j as secondary key");
        for(int row = 0; row <= pgmImage.imgHeight; row+=gridSize){
            for(int col = 0; col <= pgmImage.imgWidth; col+=gridSize){
                //converting to unitSquareMatrix co-ordinates
                int iVertex = row/gridSize;
                int jVertex = col/gridSize;


                //checking which case is it i.e. C1, C2, C3
                int caseType = checkCase(iVertex, jVertex, iMax, jMax, unitSquareMatrix);

                switch (caseType){

                    //if Case C1
                    case 1:
                        vertex = new Vertex(row, col);
                        //90 degree turn
                        vertex.angle = 0;
                        iSortedList.add(vertex);
                        if(c1flag == 1){
                            start = vertex;
                            c1flag++;
                        }
                        break;
                    case 2:
                        /*
                        if the vertex doesn't lie on the edges, then check it's candidacy of being a vertex, because
                        the vertices lying on the edge can just be a point on the Isothetic cover at the most
                         */
                        if (iVertex != 0 && iVertex != iMax && jVertex != 0 && jVertex != jMax) {
                            if ((unitSquareMatrix[iVertex][jVertex] == 1 && unitSquareMatrix[iVertex - 1][jVertex - 1] == 1 && unitSquareMatrix[iVertex][jVertex - 1] == 0 && unitSquareMatrix[iVertex - 1][jVertex] == 0) || (unitSquareMatrix[iVertex][jVertex] == 0 && unitSquareMatrix[iVertex - 1][jVertex - 1] == 0 && unitSquareMatrix[iVertex][jVertex - 1] == 1 && unitSquareMatrix[iVertex - 1][jVertex] == 1)) {
                                vertex = new Vertex(row, col);
                                vertex.angle = 1;
                                iSortedList.add(vertex);
                            }
                        }
                        break;
                    case 3:
                        vertex = new Vertex(row, col);
                        vertex.angle = 1;
                        iSortedList.add(vertex);

                        break;
                    default:
                        break;
                }
            }
        }
        LOGGER.info("iSortedList successfully created");

        //Check if no vertices in the list
        if (iSortedList.isEmpty()) {
            LOGGER.warning("No vertices in the iSortedList");
            LOGGER.warning("Exiting");
            System.exit(-1);
        }

        //iSortedList is sorted on i (Primary Key) and j (Secondary Key)

        //copying iSortedList to jSortedList
        jSortedList = new ArrayList<>(iSortedList);

        /*
        jSortedList is already sorted on i as Primary key and j as Secondary key, and Since Collections.sort() is a stable sort method,
        if we sort on j as Primary key, the order based on i won't be disturbed and i will become the Secondary key
         */
        LOGGER.info("Creating list with vertices sorted in increasing order with j as primary key and i as secondary key");
        Collections.sort(jSortedList, new VertexJComparator());
        LOGGER.info("jSortedList successfully created");

        direction = startDirection;

        LOGGER.info("Creating isotheticVertices list which stores vertices in traversal order");
        temp = start;

        //find the next vertex and add it to the vertices (list of Isothetic polygon vertices, until the next vertex is same as start, i.e. next vertex is last vertex
        do{
            isotheticVertices.add(temp);
            temp = nextVertex(temp, iSortedList, jSortedList);
        }while (temp != start);
        LOGGER.info("isotheticVertices list successfully created");

        return  isotheticVertices;
    }


    //method to find the next vertex for a vertex based on its incoming edge direction and its position in the iSortedList and jSortedList
    private static Vertex nextVertex(Vertex vertex, ArrayList<Vertex> iSortedList, ArrayList<Vertex> jSortedList){

        //Finding the position of the source vertex in the iSortedList and jSortedList
        int iPointer = iSortedList.indexOf(vertex);
        int jPointer = jSortedList.indexOf(vertex);

        //next vertex or destination vertex
        Vertex nextVertex = null;

        //find the next vertex
        switch (direction){
            case 1: //moving north
                nextVertex = jSortedList.get(--jPointer);
                break;
            case 2: //moving east
                nextVertex = iSortedList.get(++iPointer);
                break;
            case 3: //moving south
                nextVertex = jSortedList.get(++jPointer);
                break;
            case 4: //moving west
                nextVertex = iSortedList.get(--iPointer);
                break;
            default:
                System.out.println("Invalid direction");
                System.exit(-1);
                break;
        }

        //set the next direction to go
        if (nextVertex.angle == 0)
            direction--;
        else if(nextVertex.angle == 1)
            direction++;

        if(direction < 1)
            direction = 4;
        else if (direction > 4)
            direction = 1;

        return nextVertex;
    }

    //method to map i (row no.) for a horizontal edge from a vertex (i, j) in the grid
    private static int iMappingHorizontalEdge(int i, int gridSize){
        return 2*i/gridSize;
    }

    //method to map j (column no.) for any edge (horizontal or vertical) from a vertex (i, j) in the grid
    private static int jMapping(int j, int gridSize){
        return j/gridSize;
    }

    //method to map i (row no.) for a vertical edge from a vertex (i, j) in the grid
    private static int iMappingVerticalEdge(int i, int gridSize){
        return 2*i/gridSize + 1;
    }

    //method to check the case (C0, C1, C2, C3, C4), returns the no. of square containing the image, associated with a vertex
    private  static int checkCase(int iVertex, int jVertex, int iMax, int jMax, int[][] unitSquareMatrix){
        int caseType = 0;

        //if vertex lies in the middle of the grid, having all the four squares associated with it
        //need to check for all the cases i.e. C1, C2, C3
        if((iVertex >= 1 && iVertex < iMax) && (jVertex >= 1 && jVertex < jMax)){
            int iRightBottom = iVertex;
            int jRightBottom = jVertex;
            int iLeftTop = iVertex - 1;
            int jLeftTop = jVertex - 1;
            int iRightTop = iVertex - 1;
            int jRightTop = jVertex;
            int iLeftBottom = iVertex;
            int jLeftBottom = jVertex - 1;

            caseType = unitSquareMatrix[iLeftTop][jLeftTop] + unitSquareMatrix[iRightTop][jRightTop] + unitSquareMatrix[iLeftBottom][jLeftBottom] + unitSquareMatrix[iRightBottom][jRightBottom];

            //if Case is C1 and it is the first C1 vertex then check the direction of the outgoing edge
            if( caseType == 1 && c1flag == 1 && startDirection == 0){
                if(unitSquareMatrix[iLeftTop][jLeftTop] == 1)
                    startDirection = 1;
                else if (unitSquareMatrix[iRightTop][jRightTop] == 1)
                    startDirection = 2;
                else if (unitSquareMatrix[iRightBottom][jRightBottom] == 1)
                    startDirection = 3;
                else if (unitSquareMatrix[iLeftBottom][jLeftBottom] == 1)
                    startDirection = 4;
            }
        }

        //if vertex lies at the left-top corner of the grid, having only right-bottom square associated with it
        //need to check only for C1
        else if (iVertex == 0 && jVertex == 0){
            int iRightBottom = iVertex;
            int jRightBottom = jVertex;

            caseType = unitSquareMatrix[iRightBottom][jRightBottom];

            //if Case is C1 and it is the first C1 vertex then check the direction of the outgoing edge
            if( caseType == 1 && c1flag == 1 && startDirection == 0)
                startDirection = 3;
        }

        //if vertex lies at the right-top corner of the grid, having only left-bottom square associated with it
        //need to check only for C1
        else if (iVertex == 0 && jVertex == jMax){
            int iLeftBottom = iVertex;
            int jLeftBottom = jVertex - 1;

            caseType =  unitSquareMatrix[iLeftBottom][jLeftBottom];

            //if Case is C1 and it is the first C1 vertex then check the direction of the outgoing edge
            if( caseType == 1 && c1flag == 1 && startDirection == 0)
                startDirection = 4;
        }

        //if vertex lies at the left-bottom corner of the grid, having only right-top square associated with it
        //need to check only for C1
        else if (iVertex == iMax && jVertex == 0){
            int iRightTop = iVertex - 1;
            int jRightTop = jVertex;

            caseType = unitSquareMatrix[iRightTop][jRightTop];

            //if Case is C1 and it is the first C1 vertex then check the direction of the outgoing edge
            if( caseType == 1 && c1flag == 1 && startDirection == 0)
                startDirection = 2;
        }

        //if vertex lies at the right-bottom corner of the grid, having only left-top square associated with it
        //need to check only for C1
        else if (iVertex == iMax && jVertex == jMax){
            int iLeftTop = iVertex - 1;
            int jLeftTop = jVertex - 1;

            caseType =  unitSquareMatrix[iLeftTop][jLeftTop];

            //if Case is C1 and it is the first C1 vertex then check the direction of the outgoing edge
            if( caseType == 1 && c1flag == 1)
                startDirection = 1;
        }

        //if vertex lies on the top-most edge of the grid, having only two squares (left-bottm and right-bottom)associated with it
        //need to check only for C1 and C2
        else if (iVertex == 0 && jVertex >= 1 && jVertex < jMax){
            int iLeftBottom = iVertex;
            int jLeftBottom = jVertex - 1;
            int iRightBottom = iVertex;
            int jRightBottom = jVertex;

            caseType =  unitSquareMatrix[iLeftBottom][jLeftBottom] + unitSquareMatrix[iRightBottom][jRightBottom];
            if( caseType == 1 && c1flag == 1 && startDirection == 0){
                if (unitSquareMatrix[iRightBottom][jRightBottom] == 1)
                    startDirection = 3;
                else if (unitSquareMatrix[iLeftBottom][jLeftBottom] == 1)
                    startDirection = 4;
            }
        }

        //if vertex lies on the left-most edge of the grid, having only two squares (right-top and right-bottom) associated with it
        //need to check only for C1 and C2
        else if (jVertex == 0 && iVertex >= 1 && iVertex < iMax){
            int iRightTop = iVertex - 1;
            int jRightTop = jVertex;
            int iRightBottom = iVertex;
            int jRightBottom = jVertex;

            caseType = unitSquareMatrix[iRightTop][jRightTop] + unitSquareMatrix[iRightBottom][jRightBottom];
            if( caseType == 1 && c1flag == 1 && startDirection == 0){
                if (unitSquareMatrix[iRightTop][jRightTop] == 1)
                    startDirection = 2;
                else if (unitSquareMatrix[iRightBottom][jRightBottom] == 1)
                    startDirection = 3;
            }
        }

        //if vertex lies on the right-most edge of the grid, having only two squares (left-top and left-bottom) associated with it
        //need to check only for C1 and C2
        else if (jVertex == jMax && iVertex >= 1 && iVertex < iMax){
            int iLeftTop = iVertex - 1;
            int jLeftTop = jVertex - 1;
            int iLeftBottom = iVertex;
            int jLeftBottom = jVertex - 1;

            caseType =  unitSquareMatrix[iLeftTop][jLeftTop] + unitSquareMatrix[iLeftBottom][jLeftBottom];
            if( caseType == 1 && c1flag == 1 && startDirection == 0){
                if(unitSquareMatrix[iLeftTop][jLeftTop] == 1)
                    startDirection = 1;
                else if (unitSquareMatrix[iLeftBottom][jLeftBottom] == 1)
                    startDirection = 4;
            }

        }

        //if vertex lies on the bottom-most edge of the grid, having only two squares (left-top and right-top) associated with it
        //need to check only for C1 and C2
        else if (iVertex == iMax && jVertex >= 1 && jVertex < jMax){
            int iLeftTop = iVertex - 1;
            int jLeftTop = jVertex - 1;
            int iRightTop = iVertex - 1;
            int jRightTop = jVertex;

            caseType = unitSquareMatrix[iLeftTop][jLeftTop] + unitSquareMatrix[iRightTop][jRightTop];
            if( caseType == 1 && c1flag == 1 && startDirection == 0){
                if(unitSquareMatrix[iLeftTop][jLeftTop] == 1)
                    startDirection = 1;
                else if (unitSquareMatrix[iRightTop][jRightTop] == 1)
                    startDirection = 2;
            }
        }
        return caseType;
    }
}