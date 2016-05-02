import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by CNOVA on 4/16/2016.
 */
public class IsotheticOCROld {

    //variable to begin the symmetry match, first element of top most row in isothetic cover
    private int head;
    //variable to traverse in reverse direction to find the corresponding element to head
    private int tail;
    private Vertex currentHeadVertex; //vertex object at the head pointer
    private Vertex currentTailVertex; //vertex object at the tail pointer
    private Vertex prevTailVertex; //previous tail vertex
    private Vertex prevHeadVertex;
    //variable to keep track of consecutive skips
    private int skipStreak;
    private int headMoved = 0;
    private int tailMoved = 0;
    //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
    private int counter = 0;
    //variable to keep track of vertices to skip before deciding asymmetry
    private int vertexSkipThreshold;

    private PgmImage pgmImage;

    //method to check vertical symmetry
    void verticalSymmetry(PgmImage pgmImage, Properties properties, Logger LOGGER){

        this.pgmImage = pgmImage;

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        int mid = pgmImage.imgWidth/2; //mirror line

        //variable to keep track of vertices to skip before deciding asymmetry
        vertexSkipThreshold = Integer.parseInt(properties.getProperty("vertexSkipThresholdVertical"));

        //variable to store the number of grid lines allowed to look for to find a vertex corresponding to the other vertex
        int iGridErrorVertical = Integer.parseInt(properties.getProperty("iGridErrorVertical"));
        int jGridErrorVertical = Integer.parseInt(properties.getProperty("jGridErrorVertical"));


        //variable to begin the symmetry match, first element of top most row in isothetic cover
        head = pgmImage.isotheticVertices.indexOf(pgmImage.iSorted2DList.get(0).get(0));

        //variable to traverse in reverse direction to find the corresponding element to head
        tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

        //flag to know if the tail vertex corresponding to starting Head vertex has been found or not
        int found = 0;

        Vertex headVertex = pgmImage.isotheticVertices.get(head);

        int headJDifference = mid - headVertex.j;

        LOGGER.info("Finding the tail vertex corresponding to head in : " + pgmImage.sourcePgmFile.getName());
        //to find the starting value of tail (mirror of head) at i, mid + d (if head = i, mid - d)
        if (found == 0) {

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0) {

                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //perfectly mirrored vertex
                if (temp.i == headVertex.i && temp.j == mid + headJDifference && temp.angle == headVertex.angle) {
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if mirror of head not found then look for <i, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){

                Vertex temp = pgmImage.isotheticVertices.get(tail);
                //vertex one grid size before the perfectly mirrored vertex
                if (temp.i == headVertex.i && temp.j == (mid + headJDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;
            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.i == headVertex.i && temp.j == (mid + headJDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }

        }

        //if still not found then look for <i - 1, mid + d>
        if(found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex on i+g, j mirror image
                if (temp.i == headVertex.i + gridSize && temp.j == (mid + headJDifference) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.i == headVertex.i + gridSize && temp.j == (mid + headJDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.i == headVertex.i + gridSize && temp.j == (mid + headJDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if corresponding vertex not found
        if(found == 0){
            LOGGER.warning("Tail vertex corresponding to the head vertex not found in : " + pgmImage.sourcePgmFile.getName());
            //set symmetricity = 0, i.e. not symmetric
            pgmImage.verticalSymmetry = 0;
        }

        //if corresponding tail vertex found
        else {
            LOGGER.info("Tail vertex corresponding to the head vertex found in : " + pgmImage.sourcePgmFile.getName());
            //needed to save the initial TailVertex to match the ending of traversal
            Vertex tailVertex = pgmImage.isotheticVertices.get(tail);
            currentHeadVertex = pgmImage.isotheticVertices.get(head); //vertex object at the head pointer
            currentTailVertex = pgmImage.isotheticVertices.get(tail); //vertex object at the tail pointer
            prevHeadVertex = currentHeadVertex; //previous head vertex
            prevTailVertex = currentTailVertex; //previous tail vertex

            int currentHeadJDifference; //to stopre the difference of j between current head vertex and mid
            int currentTailJDifference; //to stopre the difference of j between current tail vertex and mid

            //to keep track of who is leading
            int headIDistance = 0;
            int headJDistance = 0;
            int tailIDistance = 0;
            int tailJDistance = 0;

            //variable to keep track of consecutive skips
            skipStreak = 0;
//            pgmImage.maxSkipStreak = 0;
            int breakLoop = 0;

            headMoved = 0;
            tailMoved = 0;

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            counter = 0;

            LOGGER.info("Traversing the cover to check for the symmetry of : " + pgmImage.sourcePgmFile.getName());
            do{

                System.out.println("found : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);

                //to keep track if some point has moved or not
                if (headMoved == 1) {
                    if (prevHeadVertex.i == currentHeadVertex.i) {
                        headJDistance = headJDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                    if (prevHeadVertex.j == currentHeadVertex.j) {
                        headIDistance = headIDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                }
                if (tailMoved == 1) {
                    if (prevTailVertex.i == currentTailVertex.i) {
                        tailJDistance = tailJDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                    if (prevTailVertex.j == currentTailVertex.j) {
                        tailIDistance = tailIDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                }

                //if currentTail.i is almost the same i level as currentHead.i
                if (currentTailVertex.i >= currentHeadVertex.i - iGridErrorVertical*gridSize && currentTailVertex.i <= currentHeadVertex.i + iGridErrorVertical*gridSize) {
                    System.out.println("both on the same level");

                    //distances of head and tail from the mid
                    currentHeadJDifference = mid - currentHeadVertex.j;
                    currentTailJDifference = currentTailVertex.j - mid;

                    //if currentTailVertex is corresponding to currentHeadVertex
                    if (currentTailJDifference >= currentHeadJDifference - jGridErrorVertical * gridSize && currentTailJDifference <= currentHeadJDifference + jGridErrorVertical * gridSize && currentHeadVertex.angle == currentTailVertex.angle) {
                        accept();
                        pgmImage.verticalSymmetry = 1;
                    }
                    else {
                        if (headJDistance > tailJDistance){
                            breakLoop = skipTail();
                            if (breakLoop == 1){
                                pgmImage.verticalSymmetry = 0;
                                break;
                            }
                        }
                        else if (tailJDistance > headJDistance){
                            breakLoop = skipHead();
                            if (breakLoop == 1){
                                pgmImage.verticalSymmetry = 0;
                                break;
                            }
                        }
                        else {
                            breakLoop = skipHeadAndTail();
                            if (breakLoop == 1){
                                pgmImage.verticalSymmetry = 0;
                                break;
                            }
                        }
                    }
                }

                else {
                    if (headIDistance > tailIDistance){
                        breakLoop = skipTail();
                        if (breakLoop == 1){
                            pgmImage.verticalSymmetry = 0;
                            break;
                        }
                    }
                    else if (headIDistance < tailIDistance){
                        breakLoop = skipHead();
                        if (breakLoop == 1){
                            pgmImage.verticalSymmetry = 0;
                            break;
                        }
                    }

                    else {
                        if (headJDistance > tailJDistance){
                            breakLoop = skipTail();
                            if (breakLoop == 1){
                                pgmImage.verticalSymmetry = 0;
                                break;
                            }
                        }
                        else if (headJDistance < tailJDistance){
                            breakLoop = skipHead();
                            if (breakLoop == 1){
                                pgmImage.verticalSymmetry = 0;
                                break;
                            }
                        }
                        else {
                            breakLoop = skipHeadAndTail();
                            if (breakLoop == 1){
                                pgmImage.verticalSymmetry = 0;
                                break;
                            }
                        }
                    }
                }
            }while (counter < pgmImage.isotheticVertices.size() && currentHeadVertex != headVertex && currentTailVertex != tailVertex);
        }
    }

    void accept(){
        System.out.println("almost same i distance");
        skipStreak = 0;
        System.out.println("accepted");
        head = (head + 1) % pgmImage.isotheticVertices.size();
        tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
        headMoved = 1;
        tailMoved = 1;
        prevHeadVertex = currentHeadVertex;
        currentHeadVertex = pgmImage.isotheticVertices.get(head);
        prevTailVertex = currentTailVertex;
        currentTailVertex = pgmImage.isotheticVertices.get(tail);
        counter++;
    }

    private int skipHead(){
        System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle);
        prevHeadVertex = currentHeadVertex;
        head = (head + 1) % pgmImage.isotheticVertices.size();
        headMoved = 1;
        tailMoved = 0;
        currentHeadVertex = pgmImage.isotheticVertices.get(head);
//        updateHeadDirection(currentHeadVertex, prevHeadVertex);
        counter++;
//        pgmImage.totalHeadSkip++;
        skipStreak++;
        //increase counter twice  here, because we are skipping the vertices
//        if (skipStreak > pgmImage.maxSkipStreak){
//            pgmImage.maxSkipStreak = skipStreak;
//        }
        if (skipStreak > vertexSkipThreshold) {
            return 1;
        }
        return 0;
    }

    private int skipTail(){
        System.out.println("skipping : " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
        tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
        headMoved = 0;
        tailMoved = 1;
        prevTailVertex = currentTailVertex;
        currentTailVertex = pgmImage.isotheticVertices.get(tail);
//        updateTailDirection(currentTailVertex, prevTailVertex);
//        pgmImage.totalTailSkip++;
        skipStreak++;
//        if (skipStreak > pgmImage.maxSkipStreak){
//            pgmImage.maxSkipStreak = skipStreak;
//        }
        if (skipStreak > vertexSkipThreshold) {
            return 1;
        }
        return 0;
    }

    private int skipHeadAndTail(){
        System.out.println("skipping : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);
        head = (head + 1) % pgmImage.isotheticVertices.size();
        tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
        headMoved = 1;
        tailMoved = 1;
        prevHeadVertex = currentHeadVertex;
        currentHeadVertex = pgmImage.isotheticVertices.get(head);
        prevTailVertex = currentTailVertex;
        currentTailVertex = pgmImage.isotheticVertices.get(tail);
//        updateHeadDirection(currentHeadVertex, prevHeadVertex);
//        updateTailDirection(currentTailVertex, prevTailVertex);
        counter++;
//        pgmImage.totalHeadSkip++;
//        pgmImage.totalTailSkip++;
        skipStreak++;
//        if (skipStreak > pgmImage.maxSkipStreak){
//            pgmImage.maxSkipStreak = skipStreak;
//        }
        if (skipStreak > vertexSkipThreshold) {
            return 1;
        }
        return 0;
    }

    void horizontalSymmetry(PgmImage pgmImage, Properties properties, Logger LOGGER){

        this.pgmImage = pgmImage;

        int gridSize = Integer.parseInt(properties.getProperty("gridSize"));
        int mid = pgmImage.imgHeight/2; //mirror line

        //variable to keep track of vertices to skip before deciding asymmetry
        vertexSkipThreshold = Integer.parseInt(properties.getProperty("vertexSkipThresholdHorizontal"));

        //variable to store the number of grid lines allowed to look for to find a vertex corresponding to the other vertex
        int iGridErrorHorizontal = Integer.parseInt(properties.getProperty("iGridErrorHorizontal"));
        int jGridErrorHorizontal = Integer.parseInt(properties.getProperty("jGridErrorHorizontal"));

        //variable to keep track of consecutive skips
        skipStreak = 0;

        //variable to begin the symmetry match, first element of top most row in isothetic cover
        head = pgmImage.isotheticVertices.indexOf(pgmImage.jSorted2DList.get(0).get(pgmImage.jSorted2DList.get(0).size() - 1));

        //variable to traverse in reverse direction to find the corresponding element to head
        tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

        //flag to know if the tail vertex corresponding to starting Head vertex has been found or not
        int found = 0;

        Vertex headVertex = pgmImage.isotheticVertices.get(head);

        int headIDifference = headVertex.i - mid;

        LOGGER.info("Finding the tail vertex corresponding to head in : " + pgmImage.sourcePgmFile.getName());
        //to find the starting value of tail (mirror of head) at i, mid + d (if head = i, mid - d)
        if (found == 0) {

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0) {

                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == mid - headIDifference && temp.angle == headVertex.angle) {
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if mirror of head not found then look for <i, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){

                Vertex temp = pgmImage.isotheticVertices.get(tail);
                //vertex one grid size before the perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == (mid - headIDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;
            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.j == headVertex.j && temp.i == (mid - headIDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }

        }

        //if still not found then look for <i - 1, mid + d>
        if(found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex on i+g, j mirror image
                if (temp.j == headVertex.j + gridSize && temp.i == (mid - headIDifference) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d - gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.j == headVertex.j + gridSize && temp.i == (mid - headIDifference + gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if still not found then look for <i - 1, mid + d + gridSize>
        if (found == 0){

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            int counter = 0;

            tail = (head - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();

            //check for all the vertices in the isotheticVertices list starting from the last vertex
            while (counter < pgmImage.isotheticVertices.size() && found == 0){
                Vertex temp = pgmImage.isotheticVertices.get(tail);

                //vertex one grid size after the perfectly mirrored vertex
                if (temp.j == headVertex.j + gridSize && temp.i == (mid - headIDifference - gridSize) && temp.angle == headVertex.angle){
                    found++;
                    System.out.println("found : " + temp.i + "," + temp.j + "," + temp.angle);
                    break;
                }

                tail = (tail - 1 + pgmImage.isotheticVertices.size()) % pgmImage.isotheticVertices.size();
                counter++;
            }
        }

        //if corresponding vertex not found
        if(found == 0){
            LOGGER.warning("Tail vertex corresponding to the head vertex not found in : " + pgmImage.sourcePgmFile.getName());
            //set symmetricity = 0, i.e. not symmetric
            pgmImage.horizontalSymmetry = 0;
        }

        //if corresponding vertex found
        else {
            LOGGER.info("Tail vertex corresponding to the head vertex found in : " + pgmImage.sourcePgmFile.getName());
            Vertex tailVertex = pgmImage.isotheticVertices.get(tail);
            currentHeadVertex = pgmImage.isotheticVertices.get(head); //vertex object at the head pointer
            currentTailVertex = pgmImage.isotheticVertices.get(tail); //vertex object at the tail pointer
            prevHeadVertex = currentHeadVertex; //previous head vertex
            prevTailVertex = currentTailVertex; //previous tail vertex

            int currentHeadIDifference; //to stopre the difference of j between current head vertex and mid
            int currentTailIDifference; //to stopre the difference of j between current tail vertex and mid

            //to keep track of who is leading
            int headIDistance = 0;
            int headJDistance = 0;
            int tailIDistance = 0;
            int tailJDistance = 0;

            int breakLoop = 0;
            headMoved = 0;
            tailMoved = 0;

            //counter to run the loop exactly the number of times equal to the number of vertices in the isothetic vertices list
            counter = 0;

            LOGGER.info("Traversing the cover to check for the symmetry of : " + pgmImage.sourcePgmFile.getName());
            do{

                System.out.println("found : " + currentHeadVertex.i + "," + currentHeadVertex.j + "," + currentHeadVertex.angle + " and " + currentTailVertex.i + "," + currentTailVertex.j + "," + currentTailVertex.angle);

                if (headMoved == 1) {
                    if (prevHeadVertex.i == currentHeadVertex.i) {
                        headJDistance = headJDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                    if (prevHeadVertex.j == currentHeadVertex.j) {
                        headIDistance = headIDistance + edgeLength(currentHeadVertex, prevHeadVertex);
                    }
                }
                if (tailMoved == 1) {
                    if (prevTailVertex.i == currentTailVertex.i) {
                        tailJDistance = tailJDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                    if (prevTailVertex.j == currentTailVertex.j) {
                        tailIDistance = tailIDistance + edgeLength(currentTailVertex, prevTailVertex);
                    }
                }

                //if currentTail.i is almost the same i level as currentHead.i
                if (currentTailVertex.j >= currentHeadVertex.j - jGridErrorHorizontal*gridSize && currentTailVertex.j <= currentHeadVertex.j + jGridErrorHorizontal*gridSize) {
                    System.out.println("both on the same level");

                    //distances of head and tail from the mid
                    currentHeadIDifference = currentHeadVertex.i - mid;
                    currentTailIDifference = mid - currentTailVertex.i;

                    //if currentTailVertex is corresponding to currentHeadVertex
                    if (currentTailIDifference >= currentHeadIDifference - iGridErrorHorizontal * gridSize && currentTailIDifference <= currentHeadIDifference + iGridErrorHorizontal * gridSize && currentHeadVertex.angle == currentTailVertex.angle) {
                        accept();
                        pgmImage.horizontalSymmetry = 1;
                    }
                    else {
                        if (headIDistance > tailIDistance){
                            breakLoop = skipTail();
                            if (breakLoop == 1){
                                pgmImage.horizontalSymmetry = 0;
                                break;
                            }
                        }
                        else if (tailIDistance > headIDistance){
                            breakLoop = skipHead();
                            if (breakLoop == 1){
                                pgmImage.horizontalSymmetry = 0;
                                break;
                            }
                        }
                        else {
                            breakLoop = skipHeadAndTail();
                            if (breakLoop == 1){
                                pgmImage.horizontalSymmetry = 0;
                                break;
                            }
                        }
                    }
                }
                else {
                    System.out.println("i distance not almost same");
                    if (headJDistance > tailJDistance){
                        breakLoop = skipTail();
                        if (breakLoop == 1){
                            pgmImage.horizontalSymmetry = 0;
                            break;
                        }
                    }
                    else if (headJDistance < tailJDistance){
                        breakLoop = skipHead();
                        if (breakLoop == 1){
                            pgmImage.horizontalSymmetry = 0;
                            break;
                        }
                    }
                    else {
                        if (headIDistance > tailIDistance){
                            breakLoop = skipTail();
                            if (breakLoop == 1){
                                pgmImage.horizontalSymmetry = 0;
                                break;
                            }
                        }
                        else if (headIDistance < tailIDistance){
                            breakLoop = skipHead();
                            if (breakLoop == 1){
                                pgmImage.horizontalSymmetry = 0;
                                break;
                            }
                        }
                        else {
                            breakLoop = skipHeadAndTail();
                            if (breakLoop == 1){
                                pgmImage.horizontalSymmetry = 0;
                                break;
                            }
                        }
                    }
                }
            }while (counter < pgmImage.isotheticVertices.size() && currentHeadVertex != headVertex && currentTailVertex != tailVertex);
        }
    }

    private int edgeLength(Vertex v1, Vertex v2){

        if(v1.i == v2.i){
            return Math.abs(v2.j - v1. j);
        }
        else if (v1.j == v2.j){
            return Math.abs(v2.i - v1.i);
        }
        else
            return -1;
    }
}
